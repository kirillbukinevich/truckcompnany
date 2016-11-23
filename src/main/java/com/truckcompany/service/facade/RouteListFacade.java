package com.truckcompany.service.facade;

import com.truckcompany.service.dto.RouteListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RouteListFacade {
    Page<RouteListDTO> findRouteLists(Pageable pageable);
}
