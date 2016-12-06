package com.truckcompany.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.truckcompany.domain.enums.WaybillState;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "waybill")
public class Waybill implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
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
    @JsonManagedReference
    private RouteList routeList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager")
    private User manager;

    @Column(name = "date_checked")
    private ZonedDateTime dateChecked;

    @JoinColumn (name = "waybill_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Goods> goods;

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


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ZonedDateTime getDateChecked() {
        return dateChecked;
    }

    public void setDateChecked(ZonedDateTime dateChecked) {
        this.dateChecked = dateChecked;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods) {
        this.goods = goods;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Waybill{" +
            "id=" + id +
            "number=" + number +
            "company=" + company +
            ", date=" + date +
            ", driver=" + driver +
            ", state=" + state +
            ", dispatcher=" + dispatcher +
            ", routeList=" + routeList +
            ", writeOff=" +
            '}';
    }
}
