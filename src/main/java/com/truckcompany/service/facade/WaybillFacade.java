package com.truckcompany.service.facade;

import com.truckcompany.service.dto.WaybillDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WaybillFacade {

    Page<WaybillDTO> findWaybills(Pageable pageable);

    List<WaybillDTO> findWaybills();
}
