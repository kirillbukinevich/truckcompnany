package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.TruckStatus;
import com.truckcompany.domain.enums.TruckType;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Created by Vladimir on 02.11.2016.
 */
public class TruckDTO {
    private Long id;
    private String regNumber;
    private String model;
    private Long consumption;
    private TruckType type;
    private TruckStatus status;
    private CompanyDTO company;



    public TruckDTO(){}
    public TruckDTO(Truck truck){
        this(truck.getId(), truck.getRegNumber(),truck.getModel(), truck.getConsumption(), truck.getType(),truck.getStatus(), null, emptySet());
    }

    public TruckDTO(TruckDTO truck){
        this(truck.getId(), truck.getRegNumber(), truck.getModel(), truck.getConsumption(), truck.getType(), truck.getStatus(), new Company(), emptySet());
        this.company = truck.getCompany();
    }

    public TruckDTO(Truck truck, Company company){
        this(truck.getId(), truck.getRegNumber(), truck.getModel(), truck.getConsumption(), truck.getType(), truck.getStatus(), company, emptySet());
    }

    public TruckDTO(Long id, String regNumber, String model, Long consumption, TruckType type, TruckStatus status, Company company, Set<User> users) {
        this.id = id;
        this.regNumber = regNumber;
        this.model = model;
        this.consumption = consumption;
        this.type = type;
        this.status = status;
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

    public TruckType getType() {
        return type;
    }

    public void setType(TruckType type) {
        this.type = type;
    }

    public TruckStatus getStatus() {
        return status;
    }

    public void setStatus(TruckStatus status) {
        this.status = status;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
