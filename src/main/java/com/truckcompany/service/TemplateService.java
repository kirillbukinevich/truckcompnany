package com.truckcompany.service;

import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;
import com.truckcompany.repository.TemplateRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedTemplateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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


    public Template getTemplate(long id){
        return templateRepository.findOneWithRecipientAndAdmin(id);
    }

    public List<Template> getTemplatesCreatedByCurrentAdmin(){
        if (!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) return Collections.emptyList();

        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        User admin = optionalUser.isPresent() ? optionalUser.get() : null;

        return templateRepository.findByTemplateCreatedByAdmin(admin);
    }

    public Template createTemplate(ManagedTemplateVM template){

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

}
