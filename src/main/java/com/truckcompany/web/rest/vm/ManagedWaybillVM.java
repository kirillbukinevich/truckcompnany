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

    private Long routeListId;

    private Long writeOffId;

    private ManagedRouteListVM routeList;

    private ManagedWriteOffVM writeOff;

    public ManagedWaybillVM() {
    }

    public ManagedWaybillVM(Long id, ZonedDateTime date,
                            WaybillState state, Long dispatcherId, Long driverId,
                            Long routeListId, Long writeOffId) {
        this.id = id;
        this.date = date;
        this.state = state.toString();
        this.dispatcherId = dispatcherId;
        this.driverId = driverId;
        this.routeListId = routeListId;
        this.writeOffId = writeOffId;
    }

    public ManagedWaybillVM (Waybill waybill) {
        this.id = waybill.getId();
        this.date = waybill.getDate();
        this.state = waybill.getState().toString();
        this.dispatcherId = waybill.getDispatcher().getId();
        this.driverId = waybill.getDriver().getId();
        this.routeListId = waybill.getRouteList().getId();
        this.writeOffId = waybill.getWriteOff().getId();
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

    public Long getRouteListId() {
        return routeListId;
    }

    public void setRouteListId(Long routeListId) {
        this.routeListId = routeListId;
    }

    public Long getWriteOffId() {
        return writeOffId;
    }

    public void setWriteOffId(Long writeOffId) {
        this.writeOffId = writeOffId;
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
}
