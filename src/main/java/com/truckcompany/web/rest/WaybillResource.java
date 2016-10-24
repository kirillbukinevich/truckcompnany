package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Waybill;
import com.truckcompany.repository.WaybillRepository;
import com.truckcompany.service.WaybillService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedWaybillVM;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping (value = "/api")
public class WaybillResource {

    private final Logger log = LoggerFactory.getLogger(WaybillResource.class);

    @Inject
    private WaybillRepository waybillRepository;

    @Inject
    private WaybillService waybillService;

    @RequestMapping (value = "/waybills",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Waybill>> getAllWaybills ()  throws URISyntaxException {
        log.debug("REST request get all Waybills");
        List<Waybill> waybills = waybillRepository.findAll();

        List<ManagedWaybillVM> managedWaybillVMs = waybills.stream()
            .map(ManagedWaybillVM::new)
            .collect(Collectors.toList());

        HttpHeaders headers = HeaderUtil.createAlert("waybill.getAll", null);

        return new ResponseEntity(managedWaybillVMs, headers, HttpStatus.OK);
    }

    @RequestMapping (value = "/waybills/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedWaybillVM> getWaybill (@PathVariable Long id) {
        log.debug("REST request to get Waybill : {}", id);

        Waybill waybill = waybillService.getWaybillById(id);

        if (waybill == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<ManagedWaybillVM>(new ManagedWaybillVM(waybill),HttpStatus.OK);
    }

    @RequestMapping (value = "/waybills",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createWaybill (@RequestBody ManagedWaybillVM managedWaybillVM)
        throws URISyntaxException {
        log.debug("REST request to save Waybill");
        Waybill newWaybill = waybillService.createWaybill(managedWaybillVM);

        return ResponseEntity.created(new URI("/api/companies/" + newWaybill.getId()))
            .headers(HeaderUtil.createAlert("waybill.created", newWaybill.getId().toString()))
            .body(newWaybill);
    }

    @RequestMapping (value = "/waybills/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedWaybillVM> updateWaybill (@RequestBody ManagedWaybillVM managedWaybillVM) {
        log.debug("REST request to update Waybill : {}", managedWaybillVM);
        return null;
    }

    @RequestMapping(value = "/waybills/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete Waybill: {}", id);
        waybillService.deleteWaybill(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "waybillManagement.deleted", id.toString())).build();
    }

    @RequestMapping (value = "/waybills",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity updateUser (@RequestBody ManagedWaybillVM managedWaybillVM) {
        log.debug("REST request to update Waybill : {}", managedWaybillVM);
        Waybill existingWaybill = waybillRepository.findOne(managedWaybillVM.getId());

        if (existingWaybill == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("waybillManagement", "waybilldontexist", "Waybill doesn't exist!")).body(null);

        waybillService.updateWaybill(managedWaybillVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedWaybillVM.getId().toString()))
            .body(new ManagedWaybillVM(waybillService.getWaybillById(managedWaybillVM.getId())));
    }
}
