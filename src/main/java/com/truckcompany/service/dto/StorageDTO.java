package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;

public class StorageDTO {

    private Long id;
    private String name;
    private Long companyId;

    public StorageDTO(){}

    public StorageDTO(Storage storage){
        this.id = storage.getId();
        this.name = storage.getName();
        this.companyId = storage.getCompanyId().getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCompanyId() {
        return companyId;
    }

    @Override
    public String toString() {
        return "StorageDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId='" + companyId +'\''+
                '}';
    }
}
