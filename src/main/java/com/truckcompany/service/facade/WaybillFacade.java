package com.truckcompany.service.facade;

import com.truckcompany.service.dto.WaybillDTO;

import java.util.List;

public interface WaybillFacade {

    List<WaybillDTO> findWaybills();
}
