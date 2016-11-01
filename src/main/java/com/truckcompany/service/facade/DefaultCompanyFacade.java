package com.truckcompany.service.facade;

import com.truckcompany.domain.Company;
import com.truckcompany.service.CompanyService;
import com.truckcompany.service.dto.CompanyDTO;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

import static com.truckcompany.security.AuthoritiesConstants.SUPERADMIN;
import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.stream.Collectors.toList;

/**
 * Created by Vladimir on 01.11.2016.
 */
@Component
public class DefaultCompanyFacade implements CompanyFacade {

    @Inject
    private CompanyService companyService;

    @Override
    public List<CompanyDTO> findCompanies() {
        Function<Company, CompanyDTO> toDTO = isCurrentUserInRole(SUPERADMIN) ? convertToCompanyDtoWithAdmins() : convertToCompanyDtoWithAllUsers();
        return companyService.getCompanies().stream()
            .map(toDTO)
            .collect(toList());
    }

    private Function<Company, CompanyDTO> convertToCompanyDtoWithAdmins() {
        return сompany -> new CompanyDTO(сompany, companyService.getAdminForCompany(сompany));
    }

    private Function<Company, CompanyDTO> convertToCompanyDtoWithAllUsers() {
        return сompany -> new CompanyDTO(сompany);
    }

}
