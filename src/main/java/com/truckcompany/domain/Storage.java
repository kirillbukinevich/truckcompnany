package com.truckcompany.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "storage")
public class Storage implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company companyId;

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

    public Company getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }
}
