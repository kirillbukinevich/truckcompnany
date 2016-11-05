package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import com.truckcompany.service.dto.StorageDTO;

public class ManagedStorageVM extends StorageDTO {

    public ManagedStorageVM(){
    }

    public ManagedStorageVM(Storage storage){
        super(storage);
    }

    public ManagedStorageVM(Storage storage, Company company){
        super(storage, company);
    }

    public ManagedStorageVM(StorageDTO storage){
        super(storage);
    }

}
