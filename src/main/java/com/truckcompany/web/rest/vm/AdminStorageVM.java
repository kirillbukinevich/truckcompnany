package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Storage;
import com.truckcompany.service.dto.CompanyDTO;

/**
 * Created by Vladimir on 01.11.2016.
 */
public class AdminStorageVM {
    private Long id;
    private String name;
    private boolean activated;

    public AdminStorageVM(Storage storage){
        this.id = storage.getId();
        this.name = storage.getName();
        this.activated = storage.isActivated();
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
}
