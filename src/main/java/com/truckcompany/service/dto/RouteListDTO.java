package com.truckcompany.service.dto;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Waybill;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class RouteListDTO {

    private Long id;

    private ZonedDateTime creationDate;

    private TruckDTO truck;

    private ZonedDateTime leavingDate;

    private ZonedDateTime arrivalDate;

    private StorageDTO leavingStorage;

    private StorageDTO arrivalStorage;

    private WaybillDTO waybill;

    private String state;

    private int fuelCost;

    private int distance;

    public RouteListDTO(Long id, ZonedDateTime date,
                        ZonedDateTime leavingDate, ZonedDateTime arrivalDate, String state, int fuelCost, int distance){
        this.id = id;
        this.creationDate = date;
        this.leavingDate = leavingDate;
        this.arrivalDate = arrivalDate;
        this.state = state;
        this.fuelCost = fuelCost;
        this.distance = distance;
    }

    public RouteListDTO(RouteList routeList){
        this(
            routeList.getId(),
            routeList.getCreationDate(),
            routeList.getLeavingDate(),
            routeList.getArrivalDate(),
            routeList.getState(),
            routeList.getFuelCost(),
            routeList.getDistance()
        );
        this.truck = routeList.getTruck() != null ? new TruckDTO(routeList.getTruck()) : null;
        this.leavingStorage = routeList.getLeavingStorage() != null ?
            new StorageDTO(routeList.getLeavingStorage()) : null;
        this.arrivalStorage = routeList.getArrivalStorage() != null ?
            new StorageDTO(routeList.getArrivalStorage()) : null;
/*        this.waybill = routeList.getWaybill() != null ?
            new WaybillDTO(routeList.getWaybill(), this) : null;*/
    }

    public RouteListDTO(RouteList routeList, Waybill waybill){
        this(routeList);
        this.waybill = new WaybillDTO(waybill, this);
    }

    public RouteListDTO(RouteListDTO routeListDTO){
        this(
            routeListDTO.getId(),
            routeListDTO.getCreationDate(),
            routeListDTO.getLeavingDate(),
            routeListDTO.getArrivalDate(),
            routeListDTO.getState(),
            routeListDTO.getFuelCost(),
            routeListDTO.getDistance()
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



    public ZonedDateTime getArrivalDate() {
        return arrivalDate;
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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setArrivalDate (Date arrivalDate) {
        this.arrivalDate = ZonedDateTime.ofInstant(arrivalDate.toInstant(),ZoneId.systemDefault());
    }

    public void setLeavingDate (Date leavingDate) {
        this.leavingDate = ZonedDateTime.ofInstant(leavingDate.toInstant(),ZoneId.systemDefault());
    }

    public void setLeavingZonedDate(ZonedDateTime leavingDate) {
        this.leavingDate = leavingDate;
    }

    public void setArrivalZonedDate(ZonedDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(int fuelCost) {
        this.fuelCost = fuelCost;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
