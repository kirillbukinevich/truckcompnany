package com.truckcompany.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "checkpoint")
public class Checkpoint {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "check_date")
    private Date checkDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_list_id", nullable = false)
    private RouteList routeList;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public RouteList getRouteList() {
        return routeList;
    }
    public void setRouteList(RouteList routeList) {
        this.routeList = routeList;
    }
}
