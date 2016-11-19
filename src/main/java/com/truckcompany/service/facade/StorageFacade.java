package com.truckcompany.service.facade;

import com.truckcompany.domain.Storage;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.web.rest.vm.ManagedStorageVM;

import java.util.List;

/**
 * Created by Vladimir on 01.11.2016.
 */
public interface StorageFacade {

    List<StorageDTO> findStorages();
    List<StorageDTO> findStoragesAccordingQuery(String query);
    Storage updateStorage(ManagedStorageVM managedStorageVM) throws UpdateStorageException;

}
