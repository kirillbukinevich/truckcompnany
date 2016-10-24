package com.truckcompany.service.dto;

import com.truckcompany.domain.Waybill;


/**
 * A DTO representing a Waybill.
 * Created by Viktor Dobroselsky.
 */
public class WaybillDTO {
    private Long dispatcherId;

    private Long driverId;

    private Integer routeListId;

    private Integer writeOffId;

    public WaybillDTO() {
    }

    public WaybillDTO(Long dispatcherId,Long driverId,
                      Integer routeListId,Integer writeOffId) {
        this.dispatcherId = dispatcherId;
        this.driverId = driverId;
        this.routeListId = routeListId;
        this.writeOffId = writeOffId;
    }

    public WaybillDTO(Waybill waybill) {
        this(waybill.getDispatcher().getId(),
            waybill.getDriver().getId(),
            waybill.getRouteList().getId(),
            waybill.getWriteOff().getId());
    }

    public Long getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(Long dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getRouteListId() {
        return routeListId;
    }

    public void setRouteListId(Integer routeListId) {
        this.routeListId = routeListId;
    }

    public Integer getWriteOffId() {
        return writeOffId;
    }

    public void setWriteOffId(Integer writeOffId) {
        this.writeOffId = writeOffId;
    }

    @Override
    public String toString () {
        return "WaybillDTO{" +
            "dispatcherId=" + dispatcherId +
            ", driverId=" + driverId +
            ", routeListId=" + routeListId +
            ", writeOffId=" + writeOffId +
            "}";
    }
}
