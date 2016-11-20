package com.truckcompany.service.facade;

import com.truckcompany.service.dto.RouteListDTO;

import java.util.List;

public interface RouteListFacade {
    List<RouteListDTO> findRouteLists();
}
