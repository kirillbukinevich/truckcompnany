package com.truckcompany.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "route_list")
public class RouteList implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(name = "leaving_date")
    private ZonedDateTime leavingDate;

    @Column(name = "arrival_date")
    private ZonedDateTime arrivalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaving_storage_id", nullable = false)
    private Storage leavingStorage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_storage_id", nullable = false)
    private Storage arrivalStorage;


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


}
