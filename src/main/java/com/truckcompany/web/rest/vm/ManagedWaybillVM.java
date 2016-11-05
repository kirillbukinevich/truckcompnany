package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.WriteOffAct;
import com.truckcompany.domain.enums.WaybillState;

import java.time.ZonedDateTime;

public class ManagedWaybillVM {
    private Long id;

    private ZonedDateTime date;

    private String state;

    private String dispatcherLogin;

    private String driverLogin;

    private Long routeListId;

    private Long writeOffId;

    public ManagedWaybillVM() {
    }

    public ManagedWaybillVM(Long id, ZonedDateTime date,
                            WaybillState state, String dispatcherLogin, String driverLogin,
                            Long routeListId, Long writeOffId) {
        this.id = id;
        this.date = date;
        this.state = state.toString();
        this.dispatcherLogin = dispatcherLogin;
        this.driverLogin = driverLogin;
        this.routeListId = routeListId;
        this.writeOffId = writeOffId;
    }

    public ManagedWaybillVM (Waybill waybill) {
        this(
            waybill.getId(),
            waybill.getDate(),
            waybill.getState(),
            waybill.getDispatcher().getLogin(),
            waybill.getDriver().getLogin(),
            waybill.getRouteList().getId(),
            waybill.getWriteOff() != null ? waybill.getWriteOff().getId() : null
        );

    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDispatcherLogin() {
        return dispatcherLogin;
    }

    public void setDispatcherLogin(String dispatcherLogin) {
        this.dispatcherLogin = dispatcherLogin;
    }

    public String getDriverLogin() {
        return driverLogin;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

    public Long getRouteListId() {
        return routeListId;
    }

    public void setRouteListId(Long routeListId) {
        this.routeListId = routeListId;
    }

    public Long getWriteOffId() {
        return writeOffId;
    }

    public void setWriteOffId(Long writeOffId) {
        this.writeOffId = writeOffId;
    }

    @Override
    public String toString () {
        return "ManagedWaybillVM{" +
            "id=" + id +
            ", date=" + date +
            ", state=" + state +
            ", dispatcherLogin=" + dispatcherLogin +
            ", driverLogin=" + driverLogin +
            ", routeListId=" + routeListId +
            ", writeOffId=" + writeOffId +
            "} " + super.toString();
    }
}
