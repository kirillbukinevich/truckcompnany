package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Checkpoint;
import com.truckcompany.domain.RouteList;
import com.truckcompany.service.dto.CheckpointDTO;

import java.util.Date;

public class ManagedCheckPointVM extends CheckpointDTO {

    public ManagedCheckPointVM(Long id, String name, Date checkDate, RouteList routeList) {
        super(id, name, checkDate, routeList);
    }

    public ManagedCheckPointVM(Checkpoint checkpoint) {
        super(checkpoint);
    }
}
