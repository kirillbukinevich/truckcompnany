package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Waybill;
import com.truckcompany.service.dto.UserDTO;
import com.truckcompany.service.dto.WaybillDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class ManagedWaybillVM extends WaybillDTO {
    private ManagedRouteListVM routeList;

    private ManagedWriteOffVM writeOff;

    private Long offerId;

    private Long driverId;

    private String driverLogin;

    private Set<ManagedWaybillGoodsVM> waybillGoods;

    private ManagedStorageVM arrivalStorage;

    private ManagedStorageVM leavingStorage;

    private ManagedTruckVM truck;

    public ManagedWaybillVM() {
    }

    public ManagedWaybillVM (Waybill waybill) {
        super(waybill);
        this.routeList = new ManagedRouteListVM(waybill.getRouteList());
        this.waybillGoods = waybill.getWaybillGoods()
            .stream()
            .map(ManagedWaybillGoodsVM::new)
            .collect(Collectors.toSet());
        this.arrivalStorage = new ManagedStorageVM(waybill.getRouteList().getArrivalStorage());
        this.leavingStorage = new ManagedStorageVM(waybill.getRouteList().getLeavingStorage());
        this.truck = new ManagedTruckVM(waybill.getRouteList().getTruck());
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

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public ManagedStorageVM getArrivalStorage() {
        return arrivalStorage;
    }

    public void setArrivalStorage(ManagedStorageVM arrivalStorage) {
        this.arrivalStorage = arrivalStorage;
    }

    public ManagedStorageVM getLeavingStorage() {
        return leavingStorage;
    }

    public void setLeavingStorage(ManagedStorageVM leavingStorage) {
        this.leavingStorage = leavingStorage;
    }

    public ManagedTruckVM getTruck() {
        return truck;
    }

    public void setTruck(ManagedTruckVM truck) {
        this.truck = truck;
    }

    @Override
    public String toString() {
        return "ManagedWaybillVM{" +
            "routeList=" + routeList +
            "dateChecked=" + getDateChecked() +
            ", writeOff=" + writeOff +
            ", offerId=" + offerId +
            ", driverId=" + driverId +
            ", driverLogin='" + driverLogin + '\'' +
            ", waybillGoods=" + waybillGoods +
            ", arrivalStorage=" + arrivalStorage +
            ", leavingStorage=" + leavingStorage +
            ", truck=" + truck +
            '}';
    }
}
