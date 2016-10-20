package com.truckcompany.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "route_list")
public class RouteList {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(name = "leaving_date")
    private Date leavindDate;

    @Column(name = "arrival_date")
    private Date arrivalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaving_storage_id", nullable = false)
    private Storage leavingStorageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_storage_id", nullable = false)
    private Storage arrivalStorageId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLeavindDate() {
        return leavindDate;
    }
    public void setLeavindDate(Date leavindDate) {
        this.leavindDate = leavindDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Truck getTruck() {
        return truck;
    }
    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Storage getLeavingStorageId() {
        return leavingStorageId;
    }
    public void setLeavingStorageId(Storage leavingStorageId) {
        this.leavingStorageId = leavingStorageId;
    }

    public Storage getArrivalStorageId() {
        return arrivalStorageId;
    }
    public void setArrivalStorageId(Storage arrivalStorageId) {
        this.arrivalStorageId = arrivalStorageId;
    }
}
