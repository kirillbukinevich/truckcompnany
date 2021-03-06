package com.truckcompany.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "route_list")
public class RouteList implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "leaving_date")
    private ZonedDateTime leavingDate;

    @Column(name = "arrival_date")
    private ZonedDateTime arrivalDate;

    @Column(name="fuel_cost")
    private Double fuelCost;

    @Column(name="distance")
    private Integer distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaving_storage_id", nullable = false)
    private Storage leavingStorage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_storage_id", nullable = false)
    private Storage arrivalStorage;

    @JsonBackReference
    @OneToOne(mappedBy = "routeList", fetch = FetchType.LAZY)
    private Waybill waybill;

    @Column(name = "state")
    private String state;

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

    public Truck getTruck() {
        return truck;
    }
    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Storage getLeavingStorage() {
        return leavingStorage;
    }
    public void setLeavingStorage(Storage leavingStorage) {
        this.leavingStorage = leavingStorage;
    }

    public Storage getArrivalStorage() {
        return arrivalStorage;
    }
    public void setArrivalStorage(Storage arrivalStorage) {
        this.arrivalStorage = arrivalStorage;
    }


    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Waybill getWaybill() {
        return waybill;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(Double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
