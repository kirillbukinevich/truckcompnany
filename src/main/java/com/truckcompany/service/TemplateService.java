package com.truckcompany.service;

import com.truckcompany.domain.MailError;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.MailErrorStatus;
import com.truckcompany.repository.MailErrorRepository;
import com.truckcompany.repository.TemplateRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedTemplateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @Inject
    private MailErrorRepository mailErrorRepository;


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
        newTemplate.setIsdefault(template.isdefault());

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
        template.setIsdefault(templateVM.isdefault());

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

    @Scheduled(cron = "0 0 7 * * *")
    public void sendBirtdayCards(){
        log.debug("Sending of birthdaycard has started for {}", ZonedDateTime.now());
        templateRepository.findTemplateByBirthdayToday().stream().parallel()
            .forEach( template -> mailService.sendBirthdayCard(template));
    }

    public boolean sendBirthdayCardAgain(Long errorId, String rootUploadImage){
        MailError error = mailErrorRepository.findOneWithTemplateAndRecipients(errorId);
        if (error == null) return false;
        Template template = error.getTemplate();
        log.debug("Sending of birthdayCard again for {}", template.getRecipient().getEmail());
        boolean isSuccess =  mailService.sendBirthdayCardOnce(template);
        if (isSuccess){
            error.setStatus(MailErrorStatus.SUCCESS_MANUALLY);
            mailErrorRepository.save(error);
            return true;
        }
        return false;
    }

    public void deleteMailErrors(Long[] idErrors){
        Arrays.stream(idErrors).forEach(id -> {
            MailError error = mailErrorRepository.findOne(id);
            if (error != null) {
               mailErrorRepository.delete(error);
            };
        });
    }


}
