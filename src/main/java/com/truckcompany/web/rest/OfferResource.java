package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Offer;
import com.truckcompany.domain.Truck;
import com.truckcompany.service.OfferService;

import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedOfferGoodsVM;
import com.truckcompany.web.rest.vm.ManagedOfferVM;

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
 * Created by Viktor Dobroselsky on 02.11.2016.
 */

@RestController
@RequestMapping("/api")
public class OfferResource {
    private final Logger log = LoggerFactory.getLogger(OfferResource.class);

    @Inject
    private OfferService offerService;

    @RequestMapping(value = "/offers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOffer (@RequestBody ManagedOfferVM managedOfferVM) throws URISyntaxException {
        log.debug("REST request to save Offer;");

        Offer result = offerService.createOffer(managedOfferVM);

        return ResponseEntity.created(new URI("/offer/" + result.getId()))
            .body(new ManagedOfferVM(result));
    }

    @RequestMapping (value = "/offers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedOfferVM>> getAllOffers ()  throws URISyntaxException {
        log.debug("REST request get all Offers");

        List<ManagedOfferVM> offers = offerService.getAllOffers();

        HttpHeaders headers = HeaderUtil.createAlert("offers.getAll", null);

        return new ResponseEntity(offers, headers, HttpStatus.OK);
    }

}
