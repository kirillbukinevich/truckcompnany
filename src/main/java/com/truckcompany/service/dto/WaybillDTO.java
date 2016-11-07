package com.truckcompany.service.dto;

import com.truckcompany.domain.Waybill;


/**
 * A DTO representing a Waybill.
 * Created by Viktor Dobroselsky.
 */
public class WaybillDTO {
    private Long id;

    private UserDTO dispatcher;

    private UserDTO driver;

    private RouteListDTO routeList;

    private WriteOffActDTO writeOffAct;

    public WaybillDTO() {
    }

    public WaybillDTO(Long id) {
        this.id = id;
    }

    public WaybillDTO(Waybill waybill, RouteListDTO routeList) {
        this(waybill.getId());
        this.dispatcher = waybill.getDispatcher() != null ? new UserDTO(waybill.getDispatcher()) : null;
        this.driver = waybill.getDriver() != null ? new UserDTO(waybill.getDispatcher()) : null;
        this.routeList = routeList;
        this.writeOffAct = waybill.getWriteOff() != null ? new WriteOffActDTO(waybill.getWriteOff()) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(UserDTO dispatcher) {
        this.dispatcher = dispatcher;
    }

    public UserDTO getDriver() {
        return driver;
    }

    public void setDriver(UserDTO driver) {
        this.driver = driver;
    }

    public RouteListDTO getRouteList() {
        return routeList;
    }

    public void setRouteList(RouteListDTO routeList) {
        this.routeList = routeList;
    }

    public WriteOffActDTO getWriteOffAct() {
        return writeOffAct;
    }

    public void setWriteOffAct(WriteOffActDTO writeOffAct) {
        this.writeOffAct = writeOffAct;
    }

    @Override
    public String toString () {
        return "WaybillDTO{" +
            "id=" + id +
            "dispatcherId=" + dispatcher.getId() +
            ", driverId=" + driver.getId() +
            ", routeListId=" + routeList.getId() +
            ", writeOffId=" + writeOffAct.getId() +
            "}";
    }
}
