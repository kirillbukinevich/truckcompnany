package com.truckcompany.service.facade;

import com.truckcompany.service.dto.RouteListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public interface RouteListFacade {
    Page<RouteListDTO> findRouteLists(Pageable pageable);

    Page<RouteListDTO> findRouteLists(Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate);

    List<RouteListDTO> findRouteLists();
}
