package com.truckcompany.service;

import com.truckcompany.config.JHipsterProperties;
import com.truckcompany.domain.MailError;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;

import com.truckcompany.domain.enums.MailErrorStatus;
import com.truckcompany.repository.MailErrorRepository;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;


import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.truckcompany.domain.enums.MailErrorStatus.*;
import static java.lang.System.out;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }


    @Inject
    private MailErrorRepository mailErrorRepository;

    private void saveErrorDuringSendingBirthdayCard(Template template) {
        MailError mailError = new MailError();
        mailError.setLastSending(ZonedDateTime.now());
        mailError.setTemplate(template);
        mailError.setStatus(ERROR_AUTOMATICALLY);
        mailErrorRepository.save(mailError);
    }

    private MimeMessage createMimeMessage(Template template) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
        message.setTo(template.getRecipient().getEmail());
        message.setFrom(jHipsterProperties.getMail().getFrom());
        message.setSubject(template.getName());


        Context context = new Context();
        context.setVariable("template", template);
        String content = templateEngine.process("birthdayCard", context);
        message.setText(content, true);
        return mimeMessage;
    }

    @Async
    public void sendBirthdayCard(Template template) {
        log.debug("Send birthdaycard via email to {}");

        int numberAttemptsToSend = 0;
        try {
            MimeMessage mimeMessage = prepareMimeMessageWithImage(template);

            boolean isSuccessSent = false;

            while (!isSuccessSent && numberAttemptsToSend <= 5) {
                try {
                    javaMailSender.send(mimeMessage);
                    isSuccessSent = true;
                } catch (Throwable ex) {
                    log.debug("Birthday card could not be sent to {}. Attempt #{}", template.getRecipient().getEmail(), numberAttemptsToSend);
                    numberAttemptsToSend++;
                    TimeUnit.SECONDS.sleep(5);
                }
            }
            if (isSuccessSent) {
                log.debug("Birthday card was sent success to {}.", template.getRecipient().getEmail());
            } else {
                log.debug("Message isn't delivered to {}. Number of attempts is more that 5.", template.getRecipient().getEmail());
                saveErrorDuringSendingBirthdayCard(template);
            }
        } catch (MessagingException | InterruptedException | IOException e) {
            log.debug("Message isn't delivered to {}. Cannot create Mime message.", template.getRecipient().getEmail());
            saveErrorDuringSendingBirthdayCard(template);

        }
    }

    /*public boolean sendBirthdayCardOnce(Template template) {
        log.debug("Send birthdaycard via email to {}");
        try {
            javaMailSender.send(createMimeMessage(template));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }*/


    private static class WrapMessageWithCIDMap {
        private String message;
        private Map<String, String> mapCIDs;

        WrapMessageWithCIDMap(String message) {
            this.message = message;
            this.mapCIDs = new HashMap<>();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Map<String, String> getMapCIDs() {
            return mapCIDs;
        }

        public void setMapCIDs(Map<String, String> mapCIDs) {
            this.mapCIDs = mapCIDs;
        }
    }

    private WrapMessageWithCIDMap turnRealSrcIntoCIDSrcInTemplate(String template, String directoryUploadedImage) {
        WrapMessageWithCIDMap wrapMessage = new WrapMessageWithCIDMap(template);
        Map<String, String> mapCID = wrapMessage.getMapCIDs();

        String modifiedMessage = template;

        Pattern patternSrc = Pattern.compile("src\\s*=\\s*['\"]([^'\"]+)['\"]");
        Pattern patternImage = Pattern.compile("/[^/]+$");

        Matcher matcherSrc = patternSrc.matcher(wrapMessage.getMessage());

        while (matcherSrc.find()) {
            String src = wrapMessage.getMessage().subSequence(matcherSrc.start(), matcherSrc.end()).toString();
            log.debug("Current src: {}", src);

            Matcher matcherImage = patternImage.matcher(src);
            if (matcherImage.find()) {
                String fileImage = src.subSequence(matcherImage.start() + 1, matcherImage.end() - 1).toString();
                log.debug("Current fileImage: {}", fileImage);
                String cid = generateCID();

                String srcCID = src.replaceFirst("['\"].+['\"]", "\"cid:" + cid + "\"");
                log.debug("src with CID: {}", srcCID);
                modifiedMessage = modifiedMessage.replaceFirst(src, srcCID);

                log.debug(modifiedMessage);

                mapCID.put(cid, directoryUploadedImage + File.separator + fileImage);
            }

        }

        wrapMessage.setMessage(modifiedMessage);
        return wrapMessage;
    }

    private String generateCID() {
        return UUID.randomUUID().toString().replaceAll("-", EMPTY);
    }

    private MimeMessage prepareMimeMessageWithImage(Template template ) throws MessagingException, IOException {
        String rootUploadImage = new File("src/main/webapp/content/upload/templateimage").getAbsolutePath();
        WrapMessageWithCIDMap wrapMessage = turnRealSrcIntoCIDSrcInTemplate(template.getTemplate(), rootUploadImage);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);


        Context context = new Context();
        context.setVariable("background", template.getBackground());
        context.setVariable("content", wrapMessage.getMessage());
        String content = templateEngine.process("birthdayCard", context);

        message.setText(content, true);
        message.setTo(template.getRecipient().getEmail());
        message.setFrom(jHipsterProperties.getMail().getFrom());
        message.setSubject(template.getName());

        for (Map.Entry<String, String> cid : wrapMessage.getMapCIDs().entrySet()) {
            MimeBodyPart imagePart = new MimeBodyPart();
            String urlImage = cid.getValue();
            try {
                imagePart.attachFile(urlImage);
                imagePart.setContentID("<" + cid.getKey() + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                message.getMimeMultipart().addBodyPart(imagePart);
            } catch (IOException e) {
                log.debug("Image isn't existed {}", urlImage);
                throw new IOException(e);
            }
        }

        return mimeMessage;

    }


    public boolean sendBirthdayCardOnce(Template template) {
        log.debug("Send birthday again to {}", template.getRecipient().getEmail());
        try {
            MimeMessage mimeMessage = prepareMimeMessageWithImage(template);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | IOException e) {
            log.debug("Problem with sending mail", e);
            return false;
        }

    }


    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreatePasswordForAdminEmail(User user, String baseUrl) {
        log.debug("Sending e-mail to '{}' for admin with link allowing create new password.", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("createPasswordForAdmin", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreatePasswordForEmployeeEmail(User user, String baseUrl) {
        log.debug("Sending e-mail to '{}' for employee with link allowing create new password.", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("createPasswordForEmployee", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
}
