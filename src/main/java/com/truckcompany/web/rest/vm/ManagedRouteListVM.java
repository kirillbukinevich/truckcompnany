package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.util.JSR310DateConverters;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class ManagedRouteListVM {
    private Long id;

    private Long truckId;

    private ZonedDateTime leavingDate;

    private ZonedDateTime arrivalDate;

    private Long arrival;

    private Long leaving;

    private Long leavingStorageId;

    private Long arrivalStorageId;

    public ManagedRouteListVM() {
    }

    public ManagedRouteListVM (RouteList routeList) {
        this.id = routeList.getId();
        this.truckId = routeList.getTruck().getId();
        //this.leavingDate = routeList.getLeavingDate();
        //this.arrivalDate = routeList.getArrivalDate();
        this.leavingStorageId = routeList.getLeavingStorage().getId();
        this.arrivalStorageId = routeList.getArrivalStorage().getId();
    }

    public ManagedRouteListVM(Long id, Long truckId, ZonedDateTime leavingDate,
                              ZonedDateTime arrivalDate, Long leavingStorageId,
                              Long arrivalStorageId) {
        this.id = id;
        this.truckId = truckId;
        this.leavingDate = leavingDate;
        this.arrivalDate = arrivalDate;
        this.leavingStorageId = leavingStorageId;
        this.arrivalStorageId = arrivalStorageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
    }

    public ZonedDateTime getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(ZonedDateTime leavingDate) {
        this.leavingDate = leavingDate;
    }

    public ZonedDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(ZonedDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getLeavingStorageId() {
        return leavingStorageId;
    }

    public void setLeavingStorageId(Long leavingStorageId) {
        this.leavingStorageId = leavingStorageId;
    }

    public Long getArrivalStorageId() {
        return arrivalStorageId;
    }

    public void setArrivalStorageId(Long arrivalStorageId) {
        this.arrivalStorageId = arrivalStorageId;
    }

    public Long getArrival() {
        return arrival;
    }

    public void setArrival(Long arrival) {
        this.arrival = arrival;
    }

    public Long getLeaving() {
        return leaving;
    }

    public void setLeaving(Long leaving) {
        this.leaving = leaving;
    }
}
