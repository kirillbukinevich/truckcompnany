package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Truck;
import com.truckcompany.repository.TruckRepository;
import com.truckcompany.service.TruckService;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.service.facade.TruckFacade;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.util.PaginationUtil;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import com.truckcompany.web.rest.vm.ManagedTruckVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static com.truckcompany.web.rest.util.HeaderUtil.createAlert;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Viktor Dobroselsky.
 */
@RestController
@RequestMapping("/api")
public class TruckResource {
    private final Logger LOG = LoggerFactory.getLogger(TruckResource.class);

    @Inject
    private TruckRepository truckRepository;
    @Inject
    private TruckService truckService;
    @Inject
    private TruckFacade truckFacade;

    @RequestMapping(value = "/trucks/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedTruckVM> getTruck (@PathVariable Long id) {
        LOG.debug("REST request to get Truck : {}", id);
        /*Truck truck = truckService.getTruckByIdWIthCompany(id);*/
        ManagedTruckVM truck = truckFacade.getTruckById(id);
        HttpStatus status = truck == null ? NOT_FOUND : OK;
        //ManagedTruckVM body = truck == null ? null : new ManagedTruckVM(truck, truck.getCompany());
        return new ResponseEntity<>(truck, status);
    }


    @RequestMapping (value = "/trucks", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedTruckVM>> getAllTrucks (Pageable pageable, HttpServletRequest request)  throws URISyntaxException {
        LOG.debug("REST request get all Trucks");
        Page<TruckDTO> page = truckFacade.findTrucks(pageable, request);
        List<ManagedTruckVM> managedTruckVMs = page.getContent().stream()
            .map(ManagedTruckVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trucks");
        return new ResponseEntity<List<ManagedTruckVM>>(managedTruckVMs, headers, OK);
    }


    @RequestMapping(value = "/trucks/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTruck (@PathVariable Long id) {
        LOG.debug("REST request to delete Truck: {}", id);
        truckService.deleteTruck(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "truck.deleted", id.toString())).build();
    }


    @RequestMapping (value = "/trucks", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateTruck (@RequestBody ManagedTruckVM managedTruckVM) {
        LOG.debug("REST request to update Truck : {}", managedTruckVM);
        Truck existingTruck = truckRepository.findOne(managedTruckVM.getId());
        if (existingTruck == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("truckManagement", "truckdontexist", "Truck doesn't exist!")).body(null);
        truckService.updateTruck(managedTruckVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("truckManagement.updated", managedTruckVM.getId().toString()))
            .body(new ManagedTruckVM(truckService.getTruckById(managedTruckVM.getId())));
    }


    @RequestMapping (value = "/trucks", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Truck> createTruck (@RequestBody ManagedTruckVM managedTruckVM) throws URISyntaxException {
        LOG.debug("REST request to create new Truck {}", managedTruckVM);
        Truck newTruck = truckService.createTruck(managedTruckVM);
        return ResponseEntity.created(new URI("/api/trucks/" + newTruck.getId()))
            .headers(HeaderUtil.createAlert("truck.created", newTruck.getId().toString()))
            .body(newTruck);
    }
}
