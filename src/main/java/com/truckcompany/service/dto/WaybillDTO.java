package com.truckcompany.service.dto;

import com.truckcompany.domain.Waybill;

import java.time.ZonedDateTime;


/**
 * A DTO representing a Waybill.
 * Created by Viktor Dobroselsky.
 */
public class WaybillDTO {
    private Long id;

    private ZonedDateTime date;

    private String state;

    private UserDTO dispatcher;

    private UserDTO driver;

    private RouteListDTO routeList;

    private WriteOffActDTO writeOffAct;

    public WaybillDTO() {
    }

    public WaybillDTO(Long id, ZonedDateTime date, String state) {
        this.id = id;
        this.date = date;
        this.state = state;
    }

    public WaybillDTO(Waybill waybill, RouteListDTO routeList) {
        this(waybill);
        this.routeList = routeList;
    }

    public WaybillDTO(Waybill waybill){
        this(waybill.getId(), waybill.getDate(), waybill.getState().toString());
        this.dispatcher = waybill.getDispatcher() != null ? new UserDTO(waybill.getDispatcher()) : null;
        this.driver = waybill.getDriver() != null ? new UserDTO(waybill.getDispatcher()) : null;
        this.writeOffAct = waybill.getWriteOff() != null ? new WriteOffActDTO(waybill.getWriteOff()) : null;
        this.routeList = new RouteListDTO(waybill.getRouteList().getId(), waybill.getRouteList().getDate(),
            waybill.getRouteList().getLeavingDate(), waybill.getRouteList().getArrivalDate());
    }

    public WaybillDTO(WaybillDTO waybill){
        this(waybill.getId(), waybill.getDate(), waybill.getState().toString());
        this.dispatcher = waybill.getDispatcher();
        this.driver = waybill.getDriver();
        this.writeOffAct = waybill.getWriteOffAct();
        this.routeList = waybill.getRouteList();
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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
