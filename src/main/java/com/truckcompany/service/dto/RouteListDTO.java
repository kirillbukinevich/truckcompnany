package com.truckcompany.service.dto;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.Waybill;

import javax.persistence.*;
import java.time.ZonedDateTime;

public class RouteListDTO {

    private Long id;

    private ZonedDateTime date;

    private TruckDTO truck;

    private ZonedDateTime leavingDate;

    private ZonedDateTime arrivalDate;

    private StorageDTO leavingStorage;

    private StorageDTO arrivalStorage;

    private WaybillDTO waybill;


    public RouteListDTO(Long id, ZonedDateTime date,
                        ZonedDateTime leavingDate, ZonedDateTime arrivalDate){
        this.id = id;
        this.date = date;
        this.leavingDate = leavingDate;
        this.arrivalDate = arrivalDate;
    }

    public RouteListDTO(RouteList routeList){
        this(
            routeList.getId(),
            routeList.getDate(),
            routeList.getLeavingDate(),
            routeList.getArrivalDate()
        );
        this.truck = routeList.getTruck() != null ? new TruckDTO(routeList.getTruck()) : null;
        this.leavingStorage = routeList.getLeavingStorage() != null ?
            new StorageDTO(routeList.getLeavingStorage()) : null;
        this.arrivalStorage = routeList.getArrivalStorage() != null ?
            new StorageDTO(routeList.getArrivalStorage()) : null;
        this.waybill = routeList.getWaybill() != null ?
            new WaybillDTO(routeList.getWaybill(), this) : null;
    }

    public RouteListDTO(RouteListDTO routeListDTO){
        this(
            routeListDTO.getId(),
            routeListDTO.getDate(),
            routeListDTO.getLeavingDate(),
            routeListDTO.getArrivalDate()
        );
        this.truck = routeListDTO.getTruck();
        this.leavingStorage = routeListDTO.getLeavingStorage();
        this.arrivalStorage = routeListDTO.getArrivalStorage();
        this.waybill = routeListDTO.getWaybill();
    }

    public RouteListDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TruckDTO getTruck() {
        return truck;
    }

    public void setTruck(TruckDTO truck) {
        this.truck = truck;
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

    public StorageDTO getLeavingStorage() {
        return leavingStorage;
    }

    public void setLeavingStorage(StorageDTO leavingStorage) {
        this.leavingStorage = leavingStorage;
    }

    public StorageDTO getArrivalStorage() {
        return arrivalStorage;
    }

    public void setArrivalStorage(StorageDTO arrivalStorage) {
        this.arrivalStorage = arrivalStorage;
    }

    public WaybillDTO getWaybill() {
        return waybill;
    }

    public void setWaybill(WaybillDTO waybill) {
        this.waybill = waybill;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
