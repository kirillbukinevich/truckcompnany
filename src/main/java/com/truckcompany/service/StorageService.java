package com.truckcompany.service;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StorageService {
    private final Logger LOG = LoggerFactory.getLogger(StorageService.class);

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private UserRepository userRepository;
/*
    @Transactional(readOnly = true)
    public List<Storage> getStorages () {
        log.debug("Get all storages.");
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        List<Storage> storages = storageRepository.findByCompany(user.get().getCompany());

        return storages;
    }

    public List<Storage> getAllStoragies(){
        return storageRepository.findAll();
    }*/

    @Transactional(readOnly = true)
    public Page<Storage> getStoragesBelongsCompany(Company company, Pageable page) {
        return storageRepository.findByCompanyAndNotDeleted(company, page);
    }
    @Transactional(readOnly = true)
    public Long getNumberStoragesBelongsCompany(Company company){
        return storageRepository.countStoragesBelongsCompanyAndNotDeleted(company);
    }

    @Transactional(readOnly = true)
    public Page<Storage> getStoragesBelongsCompanyAndActivated(Company company, Pageable page) {
        return storageRepository.findByCompanyAndActivated(company, true, page);
    }

    public Long getNumberStoragesBelongCompanyAndActivated(Company company){
        return storageRepository.countStoragesBelongCompanyAndNotDeletedAndActivated(company);
    }


    @Transactional(readOnly = true)
    public Storage getStorageById(Long id) {
        LOG.debug("Get Information about Storage with id: {} and company id {}", id);
        Optional<Storage> optionalStorage = storageRepository.findOneById(id);
        return optionalStorage.orElse(null);
    }

    public Storage getStorageByIdWithCompany(Long id){
        LOG.debug("Get Information about Storage with id: {} and company id {}", id);
        return storageRepository.findByIdWithCompany(id);
    }

    @Transactional
    public Storage createStorage(ManagedStorageVM managedStorageVM) {
        final Storage storage = new Storage();

        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .ifPresent(user -> {
                storage.setName(managedStorageVM.getName());
                storage.setActivated(managedStorageVM.isActivated());
                storage.setAddress(managedStorageVM.getAddress());
                storage.setCompany(user.getCompany());
                storageRepository.save(storage);
            });

        LOG.debug("Created Information for Storage: {}", storage);
        return storage;
    }

    public boolean changeStorageStatus(Long id) {
        Optional<Storage> findStorage = storageRepository.findOneById(id);
        boolean isPresent = findStorage.isPresent();
        if (isPresent) {
            Storage storage = findStorage.get();
            storage.setActivated(!storage.isActivated());
            storageRepository.save(storage);
        }
        return isPresent;
    }

    public Storage updateStorage(Storage storage) {
        LOG.debug("Changed Information for Storage: {}", storage);
        return storageRepository.save(storage);
    }
}
