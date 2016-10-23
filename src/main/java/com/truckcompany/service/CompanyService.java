package com.truckcompany.service;

import com.truckcompany.domain.Company;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by Vladimir on 21.10.2016.
 */
@Service
@Transactional
public class CompanyService {
    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Inject
    private CompanyRepository companyRepository;

    public Company createCompany(ManagedCompanyVM managedCompanyVM){
        Company company = new Company();
        company.setName(managedCompanyVM.getName());
        companyRepository.save(company);
        log.debug("Created Information for Company: {}", company);
        return company;
    }

    public Company getUserById(Long id) {
        Company company = companyRepository.getOne(id);
        log.debug("Get Information about Company with id: {}", id);
        return company;
    }


}
