package com.truckcompany.service;

import com.truckcompany.domain.Authority;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.CompanyStatus;
import com.truckcompany.repository.AuthorityRepository;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Vladimir on 21.10.2016.
 */
@Service
@Transactional
public class CompanyService {
    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private PasswordEncoder passwordEncoder;


    private Set<User> getAdminForCompany(Company company){
        return userRepository.findUsersBelongCompanyWithAuthorities(company.getId()).stream()
            .filter(user -> user.getAuthorities().contains(authorityRepository.findOne(AuthoritiesConstants.ADMIN)))
            .collect(Collectors.toSet());
    }

    public List<Company> findCompaniesAndAdmins() {
        List<Company> companies = companyRepository.findAll();
        return
            companies.stream()
                .map((company) -> {
                   /* Set<User> admins = userRepository.findUsersBelongCompanyWithAuthorities(company.getId()).stream()
                        .filter(user -> user.getAuthorities().contains(authorityRepository.findOne(AuthoritiesConstants.ADMIN)))
                        .collect(Collectors.toSet());*/
                    company.setUsers(getAdminForCompany(company));
                    return company;
                })
                .collect(Collectors.toList());
    }


    public Company findCompanyWithAdmins(Long id){
        Company company = companyRepository.getOne(id);
        company.setUsers(getAdminForCompany(company));
        return company;
    }

    public Company createCompanyWithAdmin(ManagedCompanyVM managedCompanyVM) {
        Company company = new Company();
        company.setName(managedCompanyVM.getName());
        company.setStatus(CompanyStatus.ACTIVE);

        User user = new User();
        user.setCompany(company);
        user.setLogin(managedCompanyVM.getLogin());
        user.setEmail(managedCompanyVM.getEmail());

        String encryptedPassword = passwordEncoder.encode(managedCompanyVM.getPassword());
        user.setPassword(encryptedPassword);
        user.setLangKey("en");
        user.setActivated(true);


        Authority authority = authorityRepository.findOne(AuthoritiesConstants.ADMIN);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        user.setAuthorities(authorities);
        Set<User> users = new HashSet<>();
        users.add(user);
        company.setUsers(users);

        userRepository.save(user);


        companyRepository.save(company);
        log.debug("Created Information for Company: {}", company);
        return company;
    }

    public Company getUserById(Long id) {
        Company company = companyRepository.getOne(id);
        log.debug("Get Information about Company with id: {}", id);
        return company;
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.getOne(id);
        if (company != null) {
            companyRepository.delete(company);
            log.debug("Deleted Company: {}", company);
        }
    }

    public void updateCompany(ManagedCompanyVM managedCompanyVM) {
        Company company = companyRepository.getOne(managedCompanyVM.getId());
        if (company != null) {
            company.setName(managedCompanyVM.getName());
            companyRepository.save(company);
            log.debug("Update Company: {}", company);
        }
    }


}
