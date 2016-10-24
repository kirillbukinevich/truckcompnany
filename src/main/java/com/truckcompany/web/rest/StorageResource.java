package com.truckcompany.web.rest;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.StorageRepository;
import com.truckcompany.service.StorageService;
import com.truckcompany.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies/{companyId}/storage")
public class StorageResource {

    private final Logger log = LoggerFactory.getLogger(StorageResource.class);

    @Inject
    private StorageRepository storageRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private StorageService storageService;

    @RequestMapping(value = "/{storageId}", method = RequestMethod.GET)
    ResponseEntity<Storage> getStorage(@PathVariable Long storageId, @PathVariable Long companyId){
        log.debug("REST request to get Storage : {}, from company with id: {}", storageId, companyId);
        Storage foundStorage = storageService.getStorageByIdAndCompanyId(storageId, companyId);
        if (foundStorage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundStorage, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> createStorage(@PathVariable Long companyId, @RequestBody Storage storage) throws URISyntaxException {
        log.debug("REST request to save Storage: {} ; company id: {}", storage.getName(), companyId);
        Company company = companyRepository.findOne(companyId);
        if (company == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        storage.setCompanyId(company);
        Storage result = storageRepository.save(storage);
        return ResponseEntity.created(new URI("/storage/" + result.getId()))
                .headers(HeaderUtil.createAlert( "storage.created", String.valueOf(storage.getId())))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<Storage> updateStorage(@RequestBody Storage storage, @PathVariable Long companyId){
        log.debug("REST request to update Storage : {}", storage);

        Optional<Storage> existingStorage = storageRepository.findOneByIdAndCompanyIdId(storage.getId(), companyId);
        if (!existingStorage.isPresent()){
            return new ResponseEntity<Storage>(HttpStatus.FORBIDDEN);
        }

        storage.setCompanyId(existingStorage.get().getCompanyId());
        storageRepository.save(storage);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("storage.updated", String.valueOf(storage.getId())))
                .body(storage);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{storageId}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long storageId, @PathVariable Long companyId){
        log.debug("REST request to delete Storage: {}", storageId);
        Optional<Storage> existingStorage = storageRepository.findOneByIdAndCompanyIdId(storageId, companyId);
        if (!existingStorage.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        storageRepository.delete(storageId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "storage.deleted", String.valueOf(storageId))).build();
    }


}
