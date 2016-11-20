package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Created by Vladimir on 08.11.2016.
 */
public class TemplateDTO {

    private Long id;
    private String name;
    private ZonedDateTime birthday;
    private String template;
    private String background;
    private UserDTO recipient;
    private UserDTO admin;

    public TemplateDTO() {
    }

    public TemplateDTO(Template template) {
        this(template.getId(), template.getName(), template.getBirthday(), template.getTemplate(), template.getBackground(), null, null);
    }

    public TemplateDTO(Template template, User recipient, User admin) {
        this(template.getId(), template.getName(), template.getBirthday(), template.getTemplate(), template.getBackground(), recipient, admin);
    }


    public TemplateDTO(TemplateDTO template) {
        this(template.getId(), template.getName(), template.getBirthday(), template.getTemplate(), template.getBackground(), null, null);
        this.recipient = template.getRecipient();
        this.admin = template.getAdmin();
    }

    public TemplateDTO(Long id, String name, ZonedDateTime birthday, String template, String background, User recipient, User admin) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.template = template;
        this.background = background;
        if (recipient != null) {
            this.recipient = new UserDTO(recipient);
        }
        if (admin != null) {
            this.admin = new UserDTO(admin);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }


    public UserDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDTO recipient) {
        this.recipient = recipient;
    }

    public UserDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserDTO admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "TemplateDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
