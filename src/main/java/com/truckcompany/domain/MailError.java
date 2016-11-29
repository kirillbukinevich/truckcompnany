package com.truckcompany.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.truckcompany.domain.User;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by Vladimir on 29.11.2016.
 */
@Entity
@Table(name = "mail_error")
public class MailError {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "template_id" )
    Template template;

    @Column(name = "date_last_sending", nullable = true)
    private ZonedDateTime lastSending = null;

    @Column(name = "cause")
    private String cause;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
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
}


