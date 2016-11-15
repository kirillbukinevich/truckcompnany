package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.WriteOffAct;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.service.dto.UserDTO;
import com.truckcompany.service.dto.WaybillDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class ManagedWaybillVM extends WaybillDTO {
    private ManagedRouteListVM routeList;

    private ManagedWriteOffVM writeOff;

    private Long offerId;

    private String driverLogin;

    private Set<ManagedWaybillGoodsVM> waybillGoods;

    private UserDTO driver;

    public ManagedWaybillVM() {
    }

    public ManagedWaybillVM (Waybill waybill) {
        super(waybill);
        this.routeList = new ManagedRouteListVM(waybill.getRouteList());
        this.waybillGoods = waybill.getWaybillGoods()
            .stream()
            .map(ManagedWaybillGoodsVM::new)
            .collect(Collectors.toSet());
    }

    public ManagedRouteListVM getRouteList() {
        return routeList;
    }

    public void setRouteList(ManagedRouteListVM routeList) {
        this.routeList = routeList;
    }

    public ManagedWriteOffVM getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(ManagedWriteOffVM writeOff) {
        this.writeOff = writeOff;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getDriverLogin() {
        return driverLogin;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

    public Set<ManagedWaybillGoodsVM> getWaybillGoods() {
        return waybillGoods;
    }

    public void setWaybillGoods(Set<ManagedWaybillGoodsVM> waybillGoods) {
        this.waybillGoods = waybillGoods;
    }
}
