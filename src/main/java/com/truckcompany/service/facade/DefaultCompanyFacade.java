package com.truckcompany.service.facade;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.CompanyService;
import com.truckcompany.service.dto.CompanyDTO;
import com.truckcompany.service.dto.TruckDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.truckcompany.security.AuthoritiesConstants.SUPERADMIN;
import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Created by Vladimir on 01.11.2016.
 */
@Component
public class DefaultCompanyFacade implements CompanyFacade {

    @Inject
    private CompanyService companyService;

    @Override
    public Page<CompanyDTO> findCompanies(Pageable pageable, HttpServletRequest request) {

        Function<Company, CompanyDTO> toDTO = convertToCompanyDtoWithAdmins();
        Page<Company> pageCompanies = new PageImpl<Company>(emptyList());
        pageCompanies = companyService.getPageAllCompany(pageable);
        return new PageImpl<CompanyDTO>(pageCompanies.getContent().stream().map(toDTO).collect(toList()),pageable,pageCompanies.getTotalElements());
    }



    private Function<Company, CompanyDTO> convertToCompanyDtoWithAdmins() {
        return сompany -> new CompanyDTO(сompany, companyService.getAdminForCompany(сompany));
    }

    private Function<Company, CompanyDTO> convertToCompanyDtoWithAllUsers() {
        return сompany -> new CompanyDTO(сompany);
    }

}
