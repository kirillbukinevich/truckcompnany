package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Storage;

public class StorageVM {
    Long id;
    String name;

    public StorageVM(){}

    public StorageVM(Storage storage){
        this.id = storage.getId();
        this.name = storage.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
