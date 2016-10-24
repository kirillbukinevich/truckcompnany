package com.truckcompany.service;

import com.truckcompany.domain.Storage;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.web.rest.vm.StorageVM;
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
    CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Storage getStorageByIdAndCompanyId(Long storageId, Long companyId){
        log.debug("Get Information about Storage with id: {} and company id {}", storageId, companyId);
        Optional<Storage> optionalStorage = storageRepository.findOneByIdAndCompanyIdId(storageId, companyId);
        Storage storage = null;
        if (optionalStorage.isPresent()) {
            storage = optionalStorage.get();
        }
        return storage;
    }

    public Storage createStorage(Long companyId, StorageVM storageVM){
        Storage storage = new Storage();
        storage.setName(storageVM.getName());
        storage.setCompanyId(companyRepository.findOne(companyId));
        storage = storageRepository.save(storage);
        log.debug("Created Information for Storage: {}", storage);
        return storage;
    }

    public Storage updateStorage(StorageVM storageVM){
        Storage storage = storageRepository.findOne(storageVM.getId());
        storage.setName(storageVM.getName());
        storage = storageRepository.save(storage);
        log.debug("Changed Information for Storage: {}", storage);
        return storage;
    }
}
