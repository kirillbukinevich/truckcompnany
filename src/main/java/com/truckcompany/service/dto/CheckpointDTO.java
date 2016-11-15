package com.truckcompany.service.dto;

import com.truckcompany.domain.Checkpoint;
import com.truckcompany.domain.RouteList;

import java.util.Date;

public class CheckpointDTO {
    private Long id;
    private String name;
    private Date checkDate;
    private RouteList routeList;

    public CheckpointDTO(Long id, String name, Date checkDate, RouteList routeList) {
        this.id = id;
        this.name = name;
        this.checkDate = checkDate;
        this.routeList = routeList;
    }

    public CheckpointDTO(Checkpoint checkpoint) {
        this(checkpoint.getId(), checkpoint.getName(),
            checkpoint.getCheckDate(), checkpoint.getRouteList());
    }

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
}
