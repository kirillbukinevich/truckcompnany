package com.truckcompany.service.facade;

import com.truckcompany.domain.Storage;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Vladimir on 01.11.2016.
 */
public interface StorageFacade {

    Page<StorageDTO> findStorages(Pageable page, HttpServletRequest request);
    boolean deleteStorage(Long idStorage);
    void deleteArrayStorages(Long[] idStorages);
    List<StorageDTO> findStoragesAccordingQuery(String query);
    Storage updateStorage(ManagedStorageVM managedStorageVM) throws UpdateStorageException;

}
