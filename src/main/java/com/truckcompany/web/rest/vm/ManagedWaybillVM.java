package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.enums.WaybillState;

import java.time.ZonedDateTime;

public class ManagedWaybillVM {
    private Long id;

    private ZonedDateTime date;

    private String state;

    private ManagedRouteListVM routeList;

    private ManagedWriteOffVM writeOff;

    private Long offerId;

    private ManagedUserVM driver;

    private ManagedUserVM dispatcher;

    public ManagedWaybillVM() {
    }

    public ManagedWaybillVM(Long id, ZonedDateTime date,
                            WaybillState state, Long dispatcherId, Long driverId) {
        this.id = id;
        this.date = date;
        this.state = state.toString();
    }

    public ManagedWaybillVM (Waybill waybill) {
        this.id = waybill.getId();
        this.date = waybill.getDate();
        this.state = waybill.getState().toString();
        this.dispatcher = new ManagedUserVM(waybill.getDispatcher());
        this.driver = new ManagedUserVM(waybill.getDriver());
        this.routeList = new ManagedRouteListVM(waybill.getRouteList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ManagedUserVM getDriver() {
        return driver;
    }

    public void setDriver(ManagedUserVM driver) {
        this.driver = driver;
    }

    public ManagedUserVM getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(ManagedUserVM dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ManagedRouteListVM getRouteList() {
        return routeList;
    }

    public void setRouteList(ManagedRouteListVM routeList) {
        this.routeList = routeList;
    }

    public ManagedWriteOffVM getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(ManagedWriteOffVM writeOff) {
        this.writeOff = writeOff;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}
