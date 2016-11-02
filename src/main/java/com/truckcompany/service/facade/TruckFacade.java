package com.truckcompany.service.facade;

import com.truckcompany.service.dto.TruckDTO;

import java.util.List;

/**
 * Created by Vladimir on 02.11.2016.
 */
public interface TruckFacade {
    List<TruckDTO> findTrucks();
}
