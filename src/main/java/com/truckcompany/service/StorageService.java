package com.truckcompany.service;

import com.truckcompany.domain.Storage;
import com.truckcompany.repository.StorageRepository;
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

    @Transactional(readOnly = true)
    public Storage getStorageByIdAndCompanyId(Long companyId, Long storageId){
        log.debug("Get Information about Storage with id: {} and company id {}", storageId, companyId);
        Optional<Storage> storage = storageRepository.findOneByIdAndCompanyIdId(storageId, companyId);
        if (storage.isPresent()) {
            return storage.get();
        }
        else {
            return null;
        }
    }
}
