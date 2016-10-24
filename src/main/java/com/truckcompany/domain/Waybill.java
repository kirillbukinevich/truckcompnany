package com.truckcompany.domain;


import com.truckcompany.domain.enums.WaybillState;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "waybill")
public class Waybill implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private WaybillState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatcher_id", nullable = false)
    private User dispatcher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_list_id", nullable = false)
    private RouteList routeList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "write_off_id", nullable = false)
    private WriteOffAct writeOff;

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

    public User getDriver() {
        return driver;
    }
    public void setDriver(User driver) {
        this.driver = driver;
    }

    public WaybillState getState() {
        return state;
    }
    public void setState(WaybillState state) {
        this.state = state;
    }

    public User getDispatcher() {
        return dispatcher;
    }
    public void setDispatcher(User dispatcher) {
        this.dispatcher = dispatcher;
    }

    public RouteList getRouteList() {
        return routeList;
    }
    public void setRouteList(RouteList routeList) {
        this.routeList = routeList;
    }

    public WriteOffAct getWriteOff() {
        return writeOff;
    }
    public void setWriteOff(WriteOffAct writeOff) {
        this.writeOff = writeOff;
    }

    @Override
    public String toString() {
        return "WayBill {id:" + id + "," +
                    "date:" + date + "," +
                    "driver_id:" + driver.getId() + "," +
                    "state:" + state.name() + "," +
                    "dispatcher_id:" + dispatcher.getId() + "," +
                    "route_list_id:" + routeList.getId() + "," +
                    "write_off_id:" + writeOff.getId() + "}";
    }
}
