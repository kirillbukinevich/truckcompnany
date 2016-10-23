package com.truckcompany.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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


    @JsonIgnore
    @ManyToMany
    @JoinTable (name="tc_user_company",
        joinColumns = @JoinColumn(name = "tc_company_id"),
        inverseJoinColumns = @JoinColumn(name = "jhi_user_id"))
    Set<User> users;



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

