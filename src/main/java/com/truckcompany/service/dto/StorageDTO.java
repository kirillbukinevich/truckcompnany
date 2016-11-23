package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.StorageIndex;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.CompanyStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

public class StorageDTO {

    private Long id;
    private String name;
    private boolean activated;
    private String address;
    private CompanyDTO company;

    public StorageDTO() {
    }

    public StorageDTO(StorageIndex storage){
        this(storage.getId(), storage.getName(), storage.isActivated(), storage.getAddress(), null, null);
    }

    public StorageDTO(Storage storage) {
        this(storage.getId(), storage.getName(), storage.isActivated(), storage.getAddress(), null, emptySet());
    }

    public StorageDTO(Storage storage, Company company) {
        this(storage.getId(), storage.getName(), storage.isActivated(), storage.getAddress(), company, Collections.emptySet());
    }

    public StorageDTO(Storage storage, Company company, Set<User> users) {
        this(storage.getId(), storage.getName(), storage.isActivated(), storage.getAddress(), company, users);
    }

    public StorageDTO(StorageDTO storage){
        this(storage.getId(), storage.getName(), storage.isActivated(),storage.getAddress(), new Company(), emptySet());
        this.company = storage.getCompany();
    }

    public StorageDTO(Long id, String name, boolean activated, String address, Company company, Set<User> users) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.activated = activated;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }


    @Override
    public String toString() {
        return "StorageDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", company='" + company + '\'' +
            '}';
    }
}
