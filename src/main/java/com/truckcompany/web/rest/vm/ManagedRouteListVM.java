package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.Waybill;
import com.truckcompany.service.dto.RouteListDTO;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ManagedRouteListVM extends RouteListDTO {

    public ManagedRouteListVM() {}


    public ManagedRouteListVM(RouteList routeList) {
        super(routeList);
    }

    public ManagedRouteListVM(RouteListDTO routeListDTO){
        super(routeListDTO);
    }


}
