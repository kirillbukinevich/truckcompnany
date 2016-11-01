package com.truckcompany.service.facade;

import com.truckcompany.service.dto.CompanyDTO;

import java.util.List;

/**
 * Created by Vladimir on 01.11.2016.
 */
public interface CompanyFacade {
    List<CompanyDTO> findCompanies();
}
