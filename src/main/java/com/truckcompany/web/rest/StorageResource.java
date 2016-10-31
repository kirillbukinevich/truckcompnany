package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Storage;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.service.StorageService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StorageResource {

    private final Logger log = LoggerFactory.getLogger(StorageResource.class);

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private StorageService storageService;

    @RequestMapping(value = "/storages/{storageId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedStorageVM> getStorage(@PathVariable Long storageId){
        log.debug("REST request to get Storage : {}", storageId);
        Storage foundStorage = storageService.getStorageByIdAndCompanyId(storageId);
        if (foundStorage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ManagedStorageVM(foundStorage), HttpStatus.OK);
    }

    @RequestMapping(value = "/storages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedStorageVM>> getAllStorages () throws URISyntaxException {
        log.debug("REST request get all Storages");
        List<Storage> storages = storageRepository.findAll();

        List<ManagedStorageVM> managedStorageVMs = storages.stream()
            .map(ManagedStorageVM::new)
            .collect(Collectors.toList());

        HttpHeaders headers = HeaderUtil.createAlert("storages.getAll", null);

        return new ResponseEntity(managedStorageVMs, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/storages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createStorage(@RequestBody ManagedStorageVM storage) throws URISyntaxException {
        log.debug("REST request to save Storage: {};", storage.getName());

        Storage result = storageService.createStorage(storage);

        return ResponseEntity.created(new URI("/storage/" + result.getId()))
                .headers(HeaderUtil.createAlert( "storage.created", String.valueOf(storage.getId())))
                .body(new ManagedStorageVM(result));
    }

    @RequestMapping(value = "/storages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedStorageVM> updateStorage(@RequestBody ManagedStorageVM managedStorageVM){
        log.debug("REST request to update Storage : {}", managedStorageVM);

        Storage existingStorage = storageRepository.findOne(managedStorageVM.getId());
        if (existingStorage == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Storage storage = storageService.updateStorage(managedStorageVM);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("storage.updated", String.valueOf(storage.getId())))
                .body(new ManagedStorageVM(storage));
    }

    @RequestMapping(method = RequestMethod.DELETE,
        value = "storages/{storageId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStorage(@PathVariable Long storageId){
        log.debug("REST request to delete Storage: {}", storageId);

        storageRepository.delete(storageId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "storage.deleted", String.valueOf(storageId))).build();
    }


}
