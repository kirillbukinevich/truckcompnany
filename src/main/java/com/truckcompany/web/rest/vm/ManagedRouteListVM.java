package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ManagedRouteListVM {
    private Long id;

    private Long truckId;

    private ZonedDateTime leavingDate;

    private ZonedDateTime arrivalDate;

    private Long leavingStorageId;

    private Long arrivalStorageId;


    private ManagedStorageVM leavingStorage;

    private ManagedStorageVM arrivalStorage;

    private ManagedTruckVM truck;

    private Long waybillId;

    public ManagedRouteListVM() {
    }

    public ManagedRouteListVM (RouteList routeList) {
        this(routeList.getId(),
                routeList.getTruck().getId(),
                routeList.getLeavingDate(),
                routeList.getArrivalDate(),
                routeList.getLeavingStorage().getId(),
                routeList.getArrivalStorage().getId());
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

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = ZonedDateTime.parse(arrivalDate, DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public void setLeavingDate(String leavingDate) {
        this.leavingDate = ZonedDateTime.parse(leavingDate, DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public ManagedStorageVM getLeavingStorage() {
        return leavingStorage;
    }

    public void setLeavingStorage(ManagedStorageVM leavingStorage) {
        this.leavingStorage = leavingStorage;
    }

    public ManagedStorageVM getArrivalStorage() {
        return arrivalStorage;
    }

    public void setArrivalStorage(ManagedStorageVM arrivalStorage) {
        this.arrivalStorage = arrivalStorage;
    }

    public ManagedTruckVM getTruck() {
        return truck;
    }

    public void setTruck(ManagedTruckVM truck) {
        this.truck = truck;
    }

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }
}
