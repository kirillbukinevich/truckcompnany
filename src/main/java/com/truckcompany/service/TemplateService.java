package com.truckcompany.service;

import com.sun.mail.util.MailConnectException;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;
import com.truckcompany.repository.TemplateRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedTemplateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Vladimir on 08.11.2016.
 */
@Service
@Transactional
public class TemplateService {
    private final Logger log = LoggerFactory.getLogger(TemplateService.class);

    @Inject
    private UserRepository userRepository;
    @Inject
    private TemplateRepository templateRepository;
    @Inject
    private MailService mailService;


    public Template getTemplate(long id) {
        return templateRepository.findOneWithRecipientAndAdmin(id);
    }

    public Page<Template> getTemplatesCreatedByCurrentAdmin(Pageable page) {
        if (!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) return new PageImpl<Template>(Collections.emptyList());

        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User admin = optionalUser.isPresent() ? optionalUser.get() : null;

        return templateRepository.findByTemplateCreatedByAdmin(admin, page);
    }

    public List<Template> getTemplatesCreatedByCurrentAdmin() {
        if (!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) return Collections.emptyList();

        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User admin = optionalUser.isPresent() ? optionalUser.get() : null;

        return templateRepository.findByTemplateCreatedByAdmin(admin);
    }

    public Template createTemplate(ManagedTemplateVM template) {
        Template newTemplate = new Template();
        newTemplate.setName(template.getName());
        newTemplate.setBirthday(template.getBirthday());
        newTemplate.setBackground(template.getBackground());
        newTemplate.setTemplate(template.getTemplate());

        User recepient = userRepository.getOne(template.getRecipient().getId());
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User admin = optionalUser.isPresent() ? optionalUser.get() : null;

        newTemplate.setRecipient(recepient);
        newTemplate.setAdmin(admin);

        templateRepository.save(newTemplate);

        return newTemplate;
    }

    public Template updateTemplate(ManagedTemplateVM templateVM) {
        Template template = templateRepository.findOne(templateVM.getId());

        template.setName(templateVM.getName());
        template.setBirthday(templateVM.getBirthday());
        template.setBackground(templateVM.getBackground());
        template.setTemplate(templateVM.getTemplate());

        User recepient = userRepository.getOne(templateVM.getRecipient().getId());
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User admin = optionalUser.isPresent() ? optionalUser.get() : null;

        template.setRecipient(recepient);
        template.setAdmin(admin);

        templateRepository.save(template);

        return template;

    }

    public void deleteTemplates(Long[] idTemplates) {
        for (Long id : idTemplates) {
            templateRepository.delete(id);
        }
    }


    @Scheduled(cron = "* * * * * ?")
    public void test(){
        log.debug("TEST: ");
        testAsync();
    }

    @Async
    public void testAsync(){
        throw new RuntimeException();
    }
    public void sendBirthdayCards() {
        ZonedDateTime now = ZonedDateTime.now();
        log.debug("Send birthday card at {}", now);
        List<Template> templates = templateRepository.findAll();
        for (Template template : templates){
            ZonedDateTime birthday = template.getBirthday();
            if ((now.getDayOfMonth() == birthday.getDayOfMonth() && (now.getMonth() == birthday.getMonth()))) {
                String content = template.getTemplate();
                content = "<div style='background:#efefef;'>" + content;
                content = content + "</div>";
                log.debug(content);
                try {
                    mailService.sendBirthdayCard(template.getRecipient().getEmail(), template.getName(), content);
                } catch (Throwable ex){
                    log.debug("Exception ATTENTION");
                }
            }
        }
        /*templateRepository.findAll().stream().parallel().forEach(template -> {
            ZonedDateTime birthday = template.getBirthday();
            if ((now.getDayOfMonth() == birthday.getDayOfMonth() && (now.getMonth() == birthday.getMonth()))) {
                try {
                    mailService.sendBirthdayCard(template.getRecipient().getEmail(), template.getName(), template.getTemplate());
                } catch (MailException ex) {
                    log.debug("Error");
                }
            }
        });*/

    }

}
