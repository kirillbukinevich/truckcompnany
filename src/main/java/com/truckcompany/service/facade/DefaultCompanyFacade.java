package com.truckcompany.service.facade;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.CompanyService;
import com.truckcompany.service.dto.CompanyDTO;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.service.dto.UserDTO;
import com.truckcompany.web.rest.vm.ManagedUserVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.truckcompany.security.AuthoritiesConstants.SUPERADMIN;
import static com.truckcompany.security.SecurityUtils.*;
import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Created by Vladimir on 01.11.2016.
 */
@Component
public class DefaultCompanyFacade implements CompanyFacade {

    @Inject
    private CompanyService companyService;

    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private UserRepository userRepository;

    @Override
    public Page<CompanyDTO> findCompanies(Pageable pageable, HttpServletRequest request) {

        Function<Company, CompanyDTO> toDTO = convertToCompanyDtoWithAdmins();
        Page<Company> pageCompanies = new PageImpl<Company>(emptyList());
        pageCompanies = companyService.getPageAllCompany(pageable);
        return new PageImpl<CompanyDTO>(pageCompanies.getContent().stream().map(toDTO).collect(toList()), pageable, pageCompanies.getTotalElements());
    }

    @Override
    public CompanyDTO getCompanyWithAdmin(Long id) {
        Company company = companyService.getCompanyById(id);
        if (company != null) {
            return convertToCompanyDtoWithAdmins().apply(company);
        }
        return null;
    }


    private Function<Company, CompanyDTO> convertToCompanyDtoWithAdmins() {
        return сompany -> new CompanyDTO(сompany, companyService.getAdminForCompany(сompany));
    }

    private Function<Company, CompanyDTO> convertToCompanyDtoWithAllUsers() {
        return сompany -> new CompanyDTO(сompany);
    }

    private Function<User, UserDTO> convertUserToUserDto() {
        return user -> new UserDTO(user);
    }


    public Page<UserDTO> findCompanyEmployeesBelongsAdmin(Pageable page, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (!userOptional.isPresent()) return new PageImpl<UserDTO>(emptyList());

        Function<User, UserDTO> toDTO = convertUserToUserDto();
        User admin = userOptional.get();

        List<User> foundUsers;
        long total;
        if (request.getParameter("size") == null) {
            foundUsers = userRepository.findUsersBelongsCompanyWithoutAdmin(admin.getCompany().getId());
            page = new PageRequest(0, foundUsers.size());
            total = foundUsers.size();
        } else {
            Page<User> pageUsers = userRepository.findUsersBelongsCompanyWithoutAdmin(admin.getCompany().getId(), page);
            foundUsers = pageUsers.getContent();
            total = pageUsers.getTotalElements();
        }
        List<UserDTO> content = foundUsers.stream().map(toDTO).collect(toList());
        return new PageImpl<UserDTO>(content, page, total);
    }


}
