package com.truckcompany.service;

import com.truckcompany.domain.Storage;
import com.truckcompany.domain.User;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StorageService {
    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Storage> getStorages () {
        log.debug("Get all storages.");
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        List<Storage> storages = storageRepository.findByCompany(user.get().getCompany());

        return storages;
    }

    @Transactional(readOnly = true)
    public Storage getStorageById(Long id){
        log.debug("Get Information about Storage with id: {} and company id {}", id);
        Optional<Storage> optionalStorage = storageRepository.findOneById(id);
        Storage storage = null;
        if (optionalStorage.isPresent()) {
            storage = optionalStorage.get();
        }
        return storage;
    }

    @Transactional
    public Storage createStorage(ManagedStorageVM managedStorageVM){
        final Storage storage = new Storage();

        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
        .ifPresent(user->{
            storage.setName(managedStorageVM.getName());
            storage.setCompany(user.getCompany());
            storageRepository.save(storage);
        });

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
