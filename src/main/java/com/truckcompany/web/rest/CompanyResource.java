package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.config.Constants;
import com.truckcompany.domain.Company;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.service.CompanyService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
import com.truckcompany.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by Vladimir on 20.10.2016.
 */

@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanyService companyService;


    @RequestMapping( value = "/companies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Company>> getAllTruckingCompanies(){
        log.debug("REST request get all Company");
        List<Company> truckingCompanies = companyRepository.findAll();

        HttpHeaders headers = HeaderUtil.createAlert("company.getAll", null);
        return new ResponseEntity<>(truckingCompanies, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/companies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedCompanyVM> getUser(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        Company company = companyService.getUserById(id);
        if (company == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<ManagedCompanyVM>(new ManagedCompanyVM(company), HttpStatus.OK);
    }

    @RequestMapping( value = "/companies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createTruckingCompany(@RequestBody ManagedCompanyVM managedCompanyVM) throws URISyntaxException {
        log.debug("REST request to save Company: {}", managedCompanyVM.getName());
        Company newCompany = companyService.createCompany(managedCompanyVM);


        return ResponseEntity.created(new URI("/api/companies/" + newCompany.getId()))
                .headers(HeaderUtil.createAlert("truckingCompany.created", newCompany.getId().toString()))
                .body(newCompany);
    }

    @RequestMapping(value = "/companies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedCompanyVM> updateUser(@RequestBody ManagedCompanyVM managedCompanyVM) {
        log.debug("REST request to update Company : {}", managedCompanyVM);
        return null;
    }




}
