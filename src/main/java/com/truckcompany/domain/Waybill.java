package com.truckcompany.domain;


import com.truckcompany.domain.enums.WaybillState;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "waybill")
public class Waybill {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private WaybillState state;

    @Column(name = "waybillcol")
    private String waybillcol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatcher_id", nullable = false)
    private User dispatcherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_list_id", nullable = false)
    private RouteList routeListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "write_off_id", nullable = false)
    private WriteOffAct writeOffId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public User getDriver() {
        return driver;
    }
    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getWaybillcol() {
        return waybillcol;
    }
    public void setWaybillcol(String waybillcol) {
        this.waybillcol = waybillcol;
    }

    public WaybillState getState() {
        return state;
    }
    public void setState(WaybillState state) {
        this.state = state;
    }

    public User getDispatcherId() {
        return dispatcherId;
    }
    public void setDispatcherId(User dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public RouteList getRouteListId() {
        return routeListId;
    }
    public void setRouteListId(RouteList routeListId) {
        this.routeListId = routeListId;
    }

    public WriteOffAct getWriteOffId() {
        return writeOffId;
    }
    public void setWriteOffId(WriteOffAct writeOffId) {
        this.writeOffId = writeOffId;
    }

    @Override
    public String toString() {
        return "WayBill {id:" + id + "," +
                    "date:" + date + "," +
                    "driver_id:" + driver.getId() + "," +
                    "state:" + state.name() + "," +
                    "dispatcher_id:" + dispatcherId.getId() + "," +
                    "route_list_id:" + routeListId.getId() + "," +
                    "write_off_id:" + writeOffId.getId() + "}";
    }
}
