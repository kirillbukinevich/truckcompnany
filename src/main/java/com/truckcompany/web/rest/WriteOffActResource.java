package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.WriteOffAct;
import com.truckcompany.repository.WriteOffActRepository;
import com.truckcompany.service.WriteOffActService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedWriteOffVM;
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
/**
 * Created by Viktor Dobroselsky.
 */
@RestController
@RequestMapping (value = "/api")
public class WriteOffActResource {

    private final Logger log = LoggerFactory.getLogger(WriteOffAct.class);

    @Inject
    private WriteOffActRepository writeOffActRepository;

    @Inject
    private WriteOffActService writeOffActService;

    @RequestMapping (value = "/writeoffs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WriteOffAct>> getAllWriteOff ()  throws URISyntaxException {
        log.debug("REST request get all WriteOffActs");
        List<WriteOffAct> writeOffActs = writeOffActRepository.findAll();

        List<ManagedWriteOffVM> managedWaybillVMs = writeOffActs.stream()
            .map(ManagedWriteOffVM::new)
            .collect(Collectors.toList());

        HttpHeaders headers = HeaderUtil.createAlert("waybill.getAll", null);

        return new ResponseEntity(managedWaybillVMs, headers, HttpStatus.OK);
    }

    @RequestMapping (value = "/writeoffs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WriteOffAct> getWriteOff (@PathVariable Long id)  throws URISyntaxException {
        log.debug("REST request get WriteOffAct {}" , id);
        ManagedWriteOffVM managedWriteOffVM = new ManagedWriteOffVM(writeOffActRepository.findOne(id));

        HttpHeaders headers = HeaderUtil.createAlert("waybill.getById", null);

        return new ResponseEntity(managedWriteOffVM, headers, HttpStatus.OK);
    }

    @RequestMapping (value = "/writeoffs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createWaybill (@RequestBody ManagedWriteOffVM managedWriteOffVM)
        throws URISyntaxException {
        log.debug("REST request to save WriteOffAct");
        WriteOffAct newWriteOff = writeOffActService.createWriteOffAct(managedWriteOffVM);

        return ResponseEntity.created(new URI("/api/companies/" + newWriteOff.getId()))
            .headers(HeaderUtil.createAlert("waybill.created", newWriteOff.getId().toString()))
            .body(newWriteOff);
    }

    @RequestMapping (value = "/writeoffs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity updateWaybill (@RequestBody ManagedWriteOffVM managedWriteOffVM) {
        log.debug("REST request to update Waybill : {}", managedWriteOffVM);
        WriteOffAct existingWaybill = writeOffActRepository.findOne(managedWriteOffVM.getId());

        if (existingWaybill == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("waybillManagement", "waybilldontexist", "Waybill doesn't exist!")).body(null);

        writeOffActService.updateWriteOffAct(managedWriteOffVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedWriteOffVM.getId().toString()))
            .body(new ManagedWriteOffVM(writeOffActService.getWriteOffActById(managedWriteOffVM.getId())));
    }

    @RequestMapping(value = "/writeoffs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete WriteOffAct: {}", id);
        writeOffActService.deleteWriteOffAct(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "writeOffAct.deleted", id.toString())).build();
    }

}
