package com.truckcompany.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Created by Vladimir on 08.11.2016.
 */
@Entity
@Table(name = "template")
public class Template {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private ZonedDateTime birthday;

    @Column(name = "template")
    private String template;

    @Column(name = "background")
    private String background;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private Set<MailError> mailErrors;



    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "recipient_id" )
    private User recipient;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "admin_id" )
    private User admin;

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

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
