package com.truckcompany.domain;

import javax.persistence.*;

@Entity
@Table(name = "truck")
public class Truck {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "consumption")
    private Integer consumption;

    @Column(name = "reg_number")
    private String regNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getConsumption() {
        return consumption;
    }
    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
}
