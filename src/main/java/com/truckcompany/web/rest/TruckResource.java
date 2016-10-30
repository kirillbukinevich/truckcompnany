package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Truck;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.service.TruckService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedTruckVM;
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
@RequestMapping("/api")
public class TruckResource {
    private final Logger log = LoggerFactory.getLogger(TruckResource.class);

    @Inject
    private TruckRepository truckRepository;

    @Inject
    private TruckService truckService;

    @RequestMapping(value = "/trucks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedTruckVM> getTruck (@PathVariable Long id) {
        log.debug("REST request to get Truck : {}", id);
        Truck truck = truckService.getTruckById(id);
        if (truck == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<ManagedTruckVM>(new ManagedTruckVM(truck), HttpStatus.OK);
    }

    @RequestMapping (value = "/trucks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Truck>> getAllTrucks ()  throws URISyntaxException {
        log.debug("REST request get all Trucks");
        List<Truck> trucks = truckRepository.findAll();

        List<ManagedTruckVM> managedTruckVMs = trucks.stream()
            .map(ManagedTruckVM::new)
            .collect(Collectors.toList());

        HttpHeaders headers = HeaderUtil.createAlert("truck.getAll", null);

        return new ResponseEntity(managedTruckVMs, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/trucks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity deleteTruck (@PathVariable Long id) {
        log.debug("REST request to delete Truck: {}", id);
        truckService.deleteTruck(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "truck.deleted", id.toString())).build();
    }

    @RequestMapping (value = "/trucks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity updateWaybill (@RequestBody ManagedTruckVM managedTruckVM) {
        log.debug("REST request to update Truck : {}", managedTruckVM);
        Truck existingTruck = truckRepository.findOne(managedTruckVM.getId());

        if (existingTruck == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("truckManagement", "truckdontexist", "Truck doesn't exist!")).body(null);

        truckService.updateTruck(managedTruckVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("truckManagement.updated", managedTruckVM.getId().toString()))
            .body(new ManagedTruckVM(truckService.getTruckById(managedTruckVM.getId())));
    }


    @RequestMapping (value = "/trucks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createWaybill (@RequestBody ManagedTruckVM managedTruckVM)
        throws URISyntaxException {
        log.debug("REST request to save Waybill");
        Truck newTruck = truckService.createTruck(managedTruckVM);

        return ResponseEntity.created(new URI("/api/trucks/" + newTruck.getId()))
            .headers(HeaderUtil.createAlert("truck.created", newTruck.getId().toString()))
            .body(newTruck);
    }
}
