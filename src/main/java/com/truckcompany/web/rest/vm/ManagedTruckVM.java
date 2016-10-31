package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Truck;

/**
 * Created by Viktor Dobroselsky.
 */

public class ManagedTruckVM {
    private Long id;

    private Long consumption;

    private String regNumber;

    private Long companyId;

    public ManagedTruckVM(Long id, Long consumption, String regNumber, Long companyId) {
        this.id = id;
        this.consumption = consumption;
        this.regNumber = regNumber;
        this.companyId = companyId;
    }

    public ManagedTruckVM() {
    }

    public ManagedTruckVM(Truck truck) {
        this.id = truck.getId();
        this.consumption = truck.getConsumption();
        this.regNumber = truck.getRegNumber();
        this.companyId = truck.getCompany().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsumption() {
        return consumption;
    }

    public void setConsumption(Long consumption) {
        this.consumption = consumption;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
