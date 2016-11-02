package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Created by Vladimir on 02.11.2016.
 */
public class TruckDTO {
    private Long id;
    private String regNumber;
    private Long consumption;
    private CompanyDTO company;


    public TruckDTO(){}
    public TruckDTO(Truck truck){
        this(truck.getId(), truck.getRegNumber(), truck.getConsumption(), null, emptySet());
    }

    public TruckDTO(TruckDTO truck){
        this(truck.getId(), truck.getRegNumber(), truck.getConsumption(), new Company(), emptySet());
        this.company = truck.getCompany();
    }

    public TruckDTO(Truck truck, Company company){
        this(truck.getId(), truck.getRegNumber(), truck.getConsumption(),company, emptySet());
    }

    public TruckDTO(Long id, String regNumber, Long consumption, Company company, Set<User> users) {
        this.id = id;
        this.regNumber = regNumber;
        this.consumption = consumption;
        if(company != null) {
            this.company = new CompanyDTO(company, users);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Long getConsumption() {
        return consumption;
    }

    public void setConsumption(Long consumption) {
        this.consumption = consumption;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }
}
