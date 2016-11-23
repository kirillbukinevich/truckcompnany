package com.truckcompany.service.facade;

import com.truckcompany.service.dto.WriteOffActDTO;

import java.util.List;

public interface WriteOffActFacade {

    List<WriteOffActDTO> findWriteOffActs();
}
