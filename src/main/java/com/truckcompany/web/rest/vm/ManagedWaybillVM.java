package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.WriteOffAct;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.service.dto.WaybillDTO;

import java.time.ZonedDateTime;

public class ManagedWaybillVM extends WaybillDTO{

    public ManagedWaybillVM(WaybillDTO waybillDTO){
        super(waybillDTO);
    }

    public ManagedWaybillVM(Waybill waybill){
        super(waybill);
    }
}
