package com.truckcompany.domain;

import javax.persistence.*;

@Entity
@Table(name = "truck")
public class Truck {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "consumption")
    private Integer consumption;

    @Column(name = "reg_number")
    private String regNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company companyId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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

    public Company getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }
}
