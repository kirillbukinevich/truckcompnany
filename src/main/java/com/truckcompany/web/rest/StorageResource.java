package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Storage;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.service.StorageService;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.service.facade.StorageFacade;
import com.truckcompany.service.facade.UpdateStorageException;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.AdminStorageVM;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static com.truckcompany.web.rest.util.HeaderUtil.createAlert;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class StorageResource {

    private final Logger LOG = LoggerFactory.getLogger(StorageResource.class);

    @Inject
    private StorageRepository storageRepository;
    @Inject
    private StorageService storageService;
    @Inject
    private StorageFacade storageFacade;

    @Timed
    @RequestMapping(value = "/storages/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedStorageVM> getStorage(@PathVariable Long id) {
        LOG.debug("REST request to get Storage : {}", id);
        Storage storage = storageService.getStorageByIdWithCompany(id);
        HttpStatus status = storage == null ? NOT_FOUND : OK;
        ManagedStorageVM body = storage == null ? null : new ManagedStorageVM(storage, storage.getCompany());
        return new ResponseEntity<>(body, status);
    }

    @Timed
    @RequestMapping(value = "/storages", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedStorageVM>> getAllStorages() throws URISyntaxException {
        LOG.debug("REST request get all Storages");
        List<ManagedStorageVM> managedStorageVMs = storageFacade.findStorages().stream()
            .map(ManagedStorageVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = createAlert("storages.getAll", null);
        return new ResponseEntity<>(managedStorageVMs, headers, OK);
    }

    @RequestMapping(value = "/storages", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createStorage(@RequestBody ManagedStorageVM storage) throws URISyntaxException {
        LOG.debug("REST request to save Storage: {};", storage.getName());

        Storage result = storageService.createStorage(storage);

        return ResponseEntity.created(new URI("/storage/" + result.getId()))
            .headers(createAlert("storage.created", valueOf(storage.getId())))
            .body(new ManagedStorageVM(result));
    }

    @Timed
    @RequestMapping(value = "/storages", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedStorageVM> updateStorage(@RequestBody ManagedStorageVM managedStorageVM) {
        LOG.debug("REST request to update Storage : {}", managedStorageVM);
        try {
            Storage storage = storageFacade.updateStorage(managedStorageVM);
            return ResponseEntity.ok().headers(createAlert("storage.updated", valueOf(storage.getId()))).body(new ManagedStorageVM(storage));
        } catch (UpdateStorageException e) {
            return ResponseEntity.badRequest().headers(createAlert("errorMessage", e.getMessage())).body(null);
        }
    }

    @RequestMapping(method = DELETE,
        value = "/storages/{storageId}",
        produces = APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStorage(@PathVariable Long storageId) {
        LOG.debug("REST request to delete Storage: {}", storageId);

        storageRepository.delete(storageId);
        return ResponseEntity.ok().headers(createAlert("storage.deleted", valueOf(storageId))).build();
    }


    @Timed
    @RequestMapping(value = "/storages/change_status/{storageId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeStorageStatus(@PathVariable Long storageId) {
        LOG.debug("REST request to change storage status with id: {}", storageId);

        boolean isSuccess = storageService.changeStorageStatus(storageId);
        HttpStatus status = isSuccess ? OK : BAD_REQUEST;
        return new ResponseEntity<>(status);
    }

}
