package com.truckcompany.service;

import com.truckcompany.domain.Storage;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@Transactional
public class StorageService {
    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Storage getStorageByIdAndCompanyId(Long storageId){
        log.debug("Get Information about Storage with id: {} and company id {}", storageId);
        Optional<Storage> optionalStorage = storageRepository.findOneById(storageId);
        Storage storage = null;
        if (optionalStorage.isPresent()) {
            storage = optionalStorage.get();
        }
        return storage;
    }

    public Storage createStorage(ManagedStorageVM managedStorageVM){
        Storage storage = new Storage();

        storage.setName(managedStorageVM.getName());
        storage.setCompanyId(companyRepository.findOne(managedStorageVM.getCompanyId()));

        storage = storageRepository.save(storage);

        log.debug("Created Information for Storage: {}", storage);
        return storage;
    }

    public Storage updateStorage(ManagedStorageVM managedStorageVM){
        Storage storage = storageRepository.findOne(managedStorageVM.getId());
        storage.setName(managedStorageVM.getName());
        storage = storageRepository.save(storage);
        log.debug("Changed Information for Storage: {}", storage);
        return storage;
    }
}
