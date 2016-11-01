package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.Waybill;

import java.time.ZonedDateTime;

public class ManagedRouteListVM {
    private Long id;

    private Long truckId;

    private ZonedDateTime leavingDate;

    private ZonedDateTime arrivalDate;

    private Long leavingStorageId;

    private Long arrivalStorageId;

    private Long waybillId;

    public ManagedRouteListVM() {
    }

    public ManagedRouteListVM (RouteList routeList) {
        this(routeList.getId(),
                routeList.getTruck().getId(),
                routeList.getLeavingDate(),
                routeList.getArrivalDate(),
                routeList.getLeavingStorage().getId(),
                routeList.getArrivalStorage().getId(),
                routeList.getWaybill().getId());
    }

    public ManagedRouteListVM(Long id, Long truckId, ZonedDateTime leavingDate,
                              ZonedDateTime arrivalDate, Long leavingStorageId,
                              Long arrivalStorageId, Long waybillId) {
        this.id = id;
        this.truckId = truckId;
        this.leavingDate = leavingDate;
        this.arrivalDate = arrivalDate;
        this.leavingStorageId = leavingStorageId;
        this.arrivalStorageId = arrivalStorageId;
        this.waybillId = waybillId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
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

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }
}
