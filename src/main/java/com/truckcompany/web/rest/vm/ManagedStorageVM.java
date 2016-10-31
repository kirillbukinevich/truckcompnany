package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Storage;

public class ManagedStorageVM {
    private Long id;

    private String name;

    private Long companyId;

    public ManagedStorageVM(){

    }

    public ManagedStorageVM(Storage storage){
        this.id = storage.getId();
        this.name = storage.getName();
    }

    public ManagedStorageVM(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
