package com.truckcompany.domain;


import com.truckcompany.domain.enums.WaybillState;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "route_list_id", nullable = false)
    private RouteList routeList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "write_off_id", nullable = false)
    private WriteOffAct writeOff;

    /*@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<WaybillGoods> waybillGoods;*/

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

    /*public Set<WaybillGoods> getWaybillGoods() {
        return waybillGoods;
    }

    public void setWaybillGoods(Set<WaybillGoods> waybillGoods) {
        this.waybillGoods = waybillGoods;
    }*/
}
