package com.truckcompany.domain;

import com.truckcompany.domain.enums.CompanyStatus;

import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tc_company")
public class Company implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id")
    private Long id;


    @NotNull
    @Column (name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private CompanyStatus status;

    @Column(name = "logo")
    private String logo;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn (name = "company_id")
    private Set<User> users;

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

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString(){
        return "Company{" +
            "name=" + name +
            "}";
    }
}

