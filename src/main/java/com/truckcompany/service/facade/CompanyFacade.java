package com.truckcompany.service.facade;

import com.truckcompany.service.dto.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Vladimir on 01.11.2016.
 */
public interface CompanyFacade {
    Page<CompanyDTO> findCompanies(Pageable pageable, HttpServletRequest request);
}
