package com.truckcompany.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tc_company")
public class Company {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "tc_user_company",
        joinColumns = {@JoinColumn(name = "tc_user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tc_company_id")})
    private Set<User> users;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
}
