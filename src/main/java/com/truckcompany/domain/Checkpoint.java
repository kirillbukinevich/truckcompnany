package com.truckcompany.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "checkpoint")
public class Checkpoint {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_date")
    private Date checkDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_list_id", nullable = false)
    private RouteList routeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "Checkpoint{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", checkDate=" + checkDate +
            ", routeList=" + routeList +
            '}';
    }

}
