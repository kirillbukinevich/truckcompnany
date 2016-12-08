package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Offer;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.service.OfferService;

import com.truckcompany.service.dto.OfferDTO;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.util.PaginationUtil;
import com.truckcompany.web.rest.vm.ManagedOfferVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

        if (result != null)
            return ResponseEntity.accepted().headers(HeaderUtil.createAlert("offerCrate", "accepted")).body(null);
        else
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("offer", "accessDenied", "Access denied!")).body(null);
    }

    @RequestMapping (value = "/offers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.DISPATCHER)
    public ResponseEntity<List<OfferDTO>> getAllOffers (Pageable pageable)  throws URISyntaxException {
        log.debug("REST request get all Offers");

        Page<OfferDTO> page = offerService.getAllOffers(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offers");

        return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping (value = "/offers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedOfferVM> getOffer (@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request get Offer");

        ManagedOfferVM offer = offerService.getOfferById(id);

        HttpHeaders headers = HeaderUtil.createAlert("offers.getById", null);

        return new ResponseEntity(offer, headers, HttpStatus.OK);
    }

    @RequestMapping (value = "/offers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity updateOfferState (@RequestBody ManagedOfferVM managedOfferVM) throws URISyntaxException {
        log.debug("REST request to update offer by id:", managedOfferVM.getId());

        boolean status = offerService.updateOfferState(managedOfferVM);

        if (status)
            return new ResponseEntity(HttpStatus.OK);
        else
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("offer", "accessDenied", "Access denied!")).body(null);
    }

    @RequestMapping (value = "/offers/generate",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.DISPATCHER)
    public ResponseEntity<OfferDTO> generateOffer ()  throws URISyntaxException {
        log.debug("REST request get all Offers");

        OfferDTO offer = offerService.generateOffer();

        HttpHeaders headers = HeaderUtil.createAlert("offers.generateOffer", offer.getId().toString());

        return ResponseEntity.created(new URI("/api/offers/" + offer.getId()))
            .headers(headers)
            .body(offer);
    }
}
