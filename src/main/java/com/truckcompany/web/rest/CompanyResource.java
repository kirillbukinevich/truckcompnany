package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import com.truckcompany.service.facade.CompanyFacade;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.CompanyService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.CompanyDTO;
import com.truckcompany.service.dto.UserDTO;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
import com.truckcompany.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.truckcompany.security.AuthoritiesConstants.*;
import static com.truckcompany.web.rest.util.PaginationUtil.generatePaginationHttpHeaders;
import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Vladimir on 20.10.2016.
 */

@RestController
@RequestMapping("/api")
public class CompanyResource {

    public static final String EMAIL_EXISTS = "emailexists";
    public static final String USER_EXISTS = "userexists";
    private final Logger LOG = LoggerFactory.getLogger(CompanyResource.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanyService companyService;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CompanyFacade companyFacade;


    @Secured(SUPERADMIN)
    @RequestMapping(value = "/companies", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedCompanyVM>> getAllTruckingCompanies(Pageable pageable, HttpServletRequest request) throws URISyntaxException {
        LOG.debug("REST request get all Company");

        Page<CompanyDTO> page = companyFacade.findCompanies(pageable, request);

        List<ManagedCompanyVM> managedCompanyVMs = page.getContent().stream()
            .map(c -> new ManagedCompanyVM(c))
            .collect(toList());

        HttpHeaders headers = generatePaginationHttpHeaders(page, "/api/companies");
        return new ResponseEntity<List<ManagedCompanyVM>>(managedCompanyVMs, headers, OK);
    }


    @Secured(SUPERADMIN)
    @RequestMapping(value = "/companies/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedCompanyVM> getCompany(@PathVariable Long id) {
        LOG.debug("REST request to get Company : {}", id);

        CompanyDTO company = companyFacade.getCompanyWithAdmin(id);

        if (company == null) return new ResponseEntity<>(NOT_FOUND);
        return new ResponseEntity<ManagedCompanyVM>(new ManagedCompanyVM(company), OK);
    }

    @RequestMapping(value = "/companies", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTruckingCompany(@RequestBody ManagedCompanyVM managedCompanyVM, HttpServletRequest request) throws URISyntaxException {
        LOG.debug("REST request to save Company: {}", managedCompanyVM.getName());

        UserDTO user = managedCompanyVM.getUsers().stream().limit(1).collect(toList()).get(0);

        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(user.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyManagement", EMAIL_EXISTS, "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(user.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(user.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyManagement", USER_EXISTS, "Login already in use")).body(null);
        }


        Company newCompany = companyService.createCompanyWithAdmin(managedCompanyVM, request);


        return ResponseEntity.created(new URI("/api/companies/" + newCompany.getId()))
            .headers(HeaderUtil.createAlert("truckingCompany.created", newCompany.getId().toString()))
            .body(newCompany);
    }

    @RequestMapping(value = "/companies", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedCompanyVM> updateUser(@RequestBody ManagedCompanyVM managedCompanyVM) throws URISyntaxException {
        LOG.debug("REST request to update Company : {}", managedCompanyVM);

        UserDTO user = managedCompanyVM.getUsers().stream().limit(1).collect(toList()).get(0);

        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(user.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyManagement", EMAIL_EXISTS, "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(user.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(user.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyManagement", USER_EXISTS, "Login already in use")).body(null);
        }

        companyService.updateCompany(managedCompanyVM);

        return ResponseEntity.created(new URI("/api/companies/" + managedCompanyVM.getId()))
            .headers(HeaderUtil.createAlert("truckingCompany.update", managedCompanyVM.getId().toString()))
            .body(managedCompanyVM);
    }

    @RequestMapping(value = "/change_company_status/{id}", method = GET)
    @ResponseBody
    public void changeCompanyStatus(@PathVariable Long id){
        companyService.changeCompanyStatus(id);
    }


    @RequestMapping(value = "/companies/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @Secured(SUPERADMIN)
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        LOG.debug("REST request to delete Company: {}", id);
        companyService.deleteCompany(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("truckingCompany.deleted", id.toString())).build();
    }


    @RequestMapping(value = "/companies/deleteArray", method = POST)
    public ResponseEntity<Void> deleteCompanies(@RequestBody Long[] idList){
        LOG.debug("REST request to delete list of companies {}" ,idList);
        companyService.deleteCompanies(idList);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("truckingCompany.deleted", "null")).build();
    }

    @RequestMapping(value = "/company/employee", method = GET)
    @Secured(value = {ADMIN, SUPERADMIN})
    public ResponseEntity<List<ManagedUserVM>> getCompanyEmployee(Pageable pageable, HttpServletRequest request) throws URISyntaxException {
        LOG.debug("REST request get Employees for admin with login '{}'", SecurityUtils.getCurrentUserLogin());

        Page<UserDTO> page = companyFacade.findCompanyEmployeesBelongsAdmin(pageable, request);
        List<ManagedUserVM> managedUserVMs = page.getContent().stream()
            .map(ManagedUserVM::new)
            .collect(toList());
        HttpHeaders headers = generatePaginationHttpHeaders(page, "/api/templates");
        return new ResponseEntity<>(managedUserVMs, headers, OK);
    }

    @RequestMapping(value = "/company/employee", method = POST)
    @Secured(ADMIN)
    public ResponseEntity createNewCompanyEmployee(@RequestBody ManagedUserVM managedUserVM, HttpServletRequest request){
        LOG.debug("REST request to create new Employee : '{}'", managedUserVM.getLogin());


        if (!isUniqueEmail(managedUserVM.getEmail(), managedUserVM.getId())){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", EMAIL_EXISTS, "E-mail already in use")).body(null);
        }
        if(!isUniqueLogin(managedUserVM.getLogin(), managedUserVM.getId())){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", USER_EXISTS, "Login already in use")).body(null);
        }

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(user ->{
                LOG.debug("Create new employee for company '{}'", user.getCompany().getName());
                userService.createEmployee(managedUserVM, user, request);
                return new ResponseEntity(HttpStatus.OK);
            })
            .orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value = "/company/employee", method = PUT, produces = APPLICATION_JSON_VALUE)
    @Secured({SUPERADMIN, ADMIN})
    public ResponseEntity<ManagedUserVM> updateEmployee(@RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        LOG.debug("REST request to update Employee : {}", managedUserVM.getLogin());

        if (!isUniqueEmail(managedUserVM.getEmail(), managedUserVM.getId())){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", EMAIL_EXISTS, "E-mail already in use")).body(null);
        }
        if(!isUniqueLogin(managedUserVM.getLogin(), managedUserVM.getId())){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", USER_EXISTS, "Login already in use")).body(null);
        }

        userService.updateUser(managedUserVM);
        return ResponseEntity.created(new URI("/api/company/employee" + managedUserVM.getId()))
            .headers(HeaderUtil.createAlert("ermployee.update", managedUserVM.getId().toString()))
            .body(managedUserVM);
    }

    @RequestMapping(value = "/company/employee/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @Secured({SUPERADMIN, ADMIN})
    public ResponseEntity<ManagedUserVM> getEmployee(@PathVariable Long id) {
        LOG.debug("REST request to get Employee : {}", id);
        User user = userService.getUserWithAuthorities(id);
        if (user != null){
            return new ResponseEntity<ManagedUserVM>(new ManagedUserVM(user), OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    private boolean isUniqueLogin(String login, Long idCurrentUser){
        Optional<User> existingUser = userRepository.findOneByLogin(login.toLowerCase());
        return !(existingUser.isPresent() && (!existingUser.get().getId().equals(idCurrentUser)));
    }
    private boolean isUniqueEmail(String email, Long idCurrentUser){
        Optional<User> existingUser = userRepository.findOneByEmail(email);
        return !(existingUser.isPresent() && (!existingUser.get().getId().equals(idCurrentUser)));
    }


}
