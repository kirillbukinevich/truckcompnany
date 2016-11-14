package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.enums.WaybillState;

import java.time.ZonedDateTime;

public class ManagedWaybillVM {
    private Long id;

    private ZonedDateTime date;

    private String state;

    private Long dispatcherId;

    private Long driverId;

    private ManagedRouteListVM routeList;

    private ManagedWriteOffVM writeOff;

    private Long offerId;

    public ManagedWaybillVM() {
    }

    public ManagedWaybillVM(Long id, ZonedDateTime date,
                            WaybillState state, Long dispatcherId, Long driverId) {
        this.id = id;
        this.date = date;
        this.state = state.toString();
        this.dispatcherId = dispatcherId;
        this.driverId = driverId;
    }

    public ManagedWaybillVM (Waybill waybill) {
        this.id = waybill.getId();
        this.date = waybill.getDate();
        this.state = waybill.getState().toString();
        this.dispatcherId = waybill.getDispatcher().getId();
        this.driverId = waybill.getDriver().getId();
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

    public Long getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(Long dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
