package com.truckcompany.service.dto;

import com.truckcompany.domain.MailError;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.enums.MailErrorStatus;

import java.time.ZonedDateTime;

/**
 * Created by Vladimir on 29.11.2016.
 */
public class MailErrorDTO {

    private Long id;

    private TemplateDTO template;

    private ZonedDateTime lastSending;

    private String cause;

    private MailErrorStatus status;


    public MailErrorDTO(){};

    public MailErrorDTO(Long id, ZonedDateTime lastSending, String cause, MailErrorStatus status, Template template){
        this.id = id;
        this.lastSending = lastSending;
        this.cause = cause;
        this.status = status;
        if (template != null){
            this.template = new TemplateDTO(template, template.getRecipient(), null);
        }
    }

    public MailErrorDTO(MailError mailError){
        this(mailError.getId(), mailError.getLastSending(), mailError.getCause(), mailError.getStatus(), null);
    }

    public MailErrorDTO(MailError mailError, Template template){
        this(mailError.getId(), mailError.getLastSending(), mailError.getCause(), mailError.getStatus(), template);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public ZonedDateTime getLastSending() {
        return lastSending;
    }

    public void setLastSending(ZonedDateTime lastSending) {
        this.lastSending = lastSending;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public MailErrorStatus getStatus() {
        return status;
    }

    public void setStatus(MailErrorStatus status) {
        this.status = status;
    }

    public TemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(TemplateDTO template) {
        this.template = template;
    }
}
