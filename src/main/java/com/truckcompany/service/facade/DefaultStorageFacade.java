package com.truckcompany.service.facade;

import com.truckcompany.config.JHipsterProperties;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.StorageService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Created by Vladimir on 01.11.2016.
 */
@Component
@Transactional
public class DefaultStorageFacade implements StorageFacade {
    @Inject
    private StorageService storageService;

    @Inject
    private UserService userService;

    @Override
    public List<StorageDTO> findStorages() {

        Function<Storage, StorageDTO> toDTO = convertToStorageDto();

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Storage> storages = emptyList();
            if (isCurrentUserInRole("ROLE_ADMIN")) {
                storages = storageService.getStoragesBelongsCompany(user.getCompany());
            }
            if (isCurrentUserInRole("ROLE_DISPATCHER")) {
                storages = storageService.getStoragesBelongsCompanyAndActivated(user.getCompany());
            }
            return storages.stream().map(toDTO).collect(toList());
        } else{
            return emptyList();
        }
    }

    @Override
    public Storage updateStorage(ManagedStorageVM managedStorageVM) throws UpdateStorageException {
        Storage existingStorage = storageService.getStorageById(managedStorageVM.getId());
        boolean isUserAdmin = isCurrentUserInRole("ROLE_ADMIN"); //todo security check
        if(!isUserAdmin) {
            throw new UpdateStorageException("No permission, because you're not administrator.");
        }
        boolean isValidStorage = isValidStorage(managedStorageVM.getCompany().getId()); //todo security check
        if(!isValidStorage) {
            throw new UpdateStorageException("This storage does not belong your company.");
        }
        if (existingStorage == null) {
            throw new UpdateStorageException("The storage is not found.");
        }
        existingStorage.setName(managedStorageVM.getName());
        existingStorage.setActivated(managedStorageVM.isActivated());
        return storageService.updateStorage(existingStorage);
    }


    private boolean isValidStorage(Long idCompany){
        Optional<User> user = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        return user.isPresent() ? idCompany.equals(user.get().getCompany().getId()) : false;

    }


    private Function<Storage, StorageDTO> convertToStorageDto() {
        return storage -> new StorageDTO(storage, storage.getCompany());
    }
}
