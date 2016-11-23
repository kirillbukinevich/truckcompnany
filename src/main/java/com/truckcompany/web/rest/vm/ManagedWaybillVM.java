package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Waybill;
import com.truckcompany.service.dto.WaybillDTO;


public class ManagedWaybillVM extends WaybillDTO {

    public ManagedWaybillVM(WaybillDTO waybillDTO) {

        super(waybillDTO);
    }

    public ManagedWaybillVM(Waybill waybill) {
        super(waybill);
    }

    public ManagedWaybillVM() {

    }
}
