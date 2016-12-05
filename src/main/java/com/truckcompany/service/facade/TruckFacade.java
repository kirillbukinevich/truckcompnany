package com.truckcompany.service.facade;

import com.truckcompany.domain.Truck;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.web.rest.TruckResource;
import com.truckcompany.web.rest.vm.ManagedTruckVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Vladimir on 02.11.2016.
 */
public interface TruckFacade {
    public Page<TruckDTO> findTrucks(Pageable pageable, HttpServletRequest request);
    ManagedTruckVM getTruckById(Long id);
}
