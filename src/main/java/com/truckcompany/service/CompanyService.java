package com.truckcompany.service;

import com.truckcompany.domain.Authority;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.CompanyStatus;
import com.truckcompany.repository.AuthorityRepository;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.service.dto.CompanyDTO;
import com.truckcompany.service.dto.UserDTO;
import com.truckcompany.web.rest.vm.ManagedCompanyVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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

    @Inject
    private MailService mailService;


    public Set<User> getAdminForCompany(Company company) {
        return userRepository.findUsersBelongCompanyWithAuthorities(company.getId()).stream()
            .filter(user -> user.getAuthorities().contains(authorityRepository.findOne(AuthoritiesConstants.ADMIN)))
            .collect(toSet());
    }

    public List<Company> findCompaniesAndAdmins() {
        List<Company> companies = getCompanies();
        return
            companies.stream()
                .map((company) -> {
                   /* Set<User> admins = userRepository.findUsersBelongCompanyWithAuthorities(company.getId()).stream()
                        .filter(user -> user.getAuthorities().contains(authorityRepository.findOne(AuthoritiesConstants.ADMIN)))
                        .collect(Collectors.toSet());*/
                    company.setUsers(getAdminForCompany(company));
                    return company;
                })
                .collect(toList());
    }

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public Company findCompanyWithAdmins(Long id) {
        Company company = companyRepository.getOne(id);
        company.setUsers(getAdminForCompany(company));
        return company;
    }

    public Company createCompanyWithAdmin(ManagedCompanyVM managedCompanyVM, HttpServletRequest request) {
        Company company = new Company();
        company.setName(managedCompanyVM.getName());
        company.setStatus(CompanyStatus.ACTIVE);

        UserDTO userFromForm = managedCompanyVM.getUsers().stream().limit(1).collect(toList()).get(0);

        User user = new User();
        user.setCompany(company);
        user.setLogin(userFromForm.getLogin());
        user.setEmail(userFromForm.getEmail());
        user.setActivationKey(passwordEncoder.encode(new Date().toString()).substring(0, 20));
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

        String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context

        mailService.sendCreatePasswordForAdminEmail(user, baseUrl);


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

    public void deleteCompanies(Long[] idList) {
        Arrays.stream(idList)
            .forEach(id -> {
                Company company = companyRepository.getOne(id);
                companyRepository.delete(company);
            });
    }

    public void updateCompany(ManagedCompanyVM managedCompanyVM) {
        Company company = companyRepository.getOne(managedCompanyVM.getId());
        if (company != null) {
            company.setName(managedCompanyVM.getName());

            UserDTO userFromForm = managedCompanyVM.getUsers().stream().limit(1).collect(toList()).get(0);

            User user = userRepository.findOne(userFromForm.getId());
            if (user!=null) {
                user.setLogin(userFromForm.getLogin());
                user.setEmail(userFromForm.getEmail());
                userRepository.save(user);
            }

            companyRepository.save(company);
            log.debug("Update Company: {}", company);
        }
    }

    public void changeCompanyStatus(Long id) {
        Company company = companyRepository.getOne(id);

        CompanyStatus status = company.getStatus();
        if (status == CompanyStatus.ACTIVE) {
            company.setStatus(CompanyStatus.DEACTIVATE);
        } else {
            company.setStatus(CompanyStatus.ACTIVE);
        }

        companyRepository.save(company);

    }

    public List<User> getCompanyUsersWithoutAdmin(User user) {
        return userRepository.findUsersBelongCompanyWithAuthorities(user.getCompany().getId()).stream()
            .filter(hasAuthority(AuthoritiesConstants.ADMIN).negate())
            .collect(toList());
    }

    private Predicate<? super User> hasAuthority(String role) {
        return user -> user.getAuthorities().stream()
            .filter(authority -> StringUtils.equals(authority.getName(), role))
            .findFirst()
            .map(authority -> true)
            .orElse(false);
    }


}
