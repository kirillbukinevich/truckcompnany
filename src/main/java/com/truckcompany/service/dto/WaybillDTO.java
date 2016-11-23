package com.truckcompany.service.dto;

import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.WaybillGoods;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.web.rest.vm.ManagedWaybillGoodsVM;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * A DTO representing a Waybill.
 * Created by Viktor Dobroselsky.
 */
public class WaybillDTO {
    private Long id;

    private ZonedDateTime date;

    private RouteListDTO routeList;

    private WriteOffActDTO writeOffAct;

    private UserDTO driver;

    private WaybillState state;

    private UserDTO dispatcher;

    private UserDTO manager;

    private ZonedDateTime dateChecked;

    private OfferDTO offer;

    private Set<ManagedWaybillGoodsVM> waybillGoods;

    public WaybillDTO(Long id, ZonedDateTime date, UserDTO driver, WaybillState state, UserDTO dispatcher, UserDTO manager, ZonedDateTime dateChecked) {
        this.id = id;
        this.date = date;
        this.driver = driver;
        this.state = state;
        this.dispatcher = dispatcher;
        this.manager = manager;
        this.dateChecked = dateChecked;
    }

    public WaybillDTO(Long id, ZonedDateTime date, UserDTO driver, WaybillState state, UserDTO dispatcher) {
        this.id = id;
        this.date = date;
        this.driver = driver;
        this.state = state;
        this.dispatcher = dispatcher;
    }

    public WaybillDTO() {
    }

    public WaybillDTO(Long id, ZonedDateTime date, WaybillState state) {
        this.id = id;
        this.date = date;
        this.state = state;
    }


    public WaybillDTO(Waybill waybill, RouteListDTO routeList) {
        this(waybill);
        this.routeList = routeList;
    }


    public WaybillDTO(Waybill waybill) {
        this(waybill.getId(), waybill.getDate(), waybill.getState());
        this.dispatcher = waybill.getDispatcher() != null ? new UserDTO(waybill.getDispatcher()) : null;
        this.driver = waybill.getDriver() != null ? new UserDTO(waybill.getDriver()) : null;
        this.writeOffAct = waybill.getWriteOff() != null ? new WriteOffActDTO(waybill.getWriteOff()) : null;
        this.routeList = new RouteListDTO(waybill.getRouteList());
        this.manager = waybill.getManager() == null ? null : new UserDTO(waybill.getManager());
        this.dateChecked = waybill.getDateChecked();
        this.waybillGoods = waybill.getWaybillGoods()
            .stream()
            .map(ManagedWaybillGoodsVM::new)
            .collect(Collectors.toSet());

        //this.offer = new OfferDTO(waybill.getOffer);
    }

    public WaybillDTO(WaybillDTO waybill) {
        this(waybill.getId(), waybill.getDate(), waybill.getState());
        this.dispatcher = waybill.getDispatcher();
        this.driver = waybill.getDriver();
        this.writeOffAct = waybill.getWriteOffAct();
        this.routeList = waybill.getRouteList();
        this.offer = waybill.getOffer();
        this.waybillGoods = waybill.getWaybillGoods();
    }

    public RouteListDTO getRouteList() {
        return routeList;
    }

    public void setRouteList(RouteListDTO routeList) {
        this.routeList = routeList;
    }

    public WriteOffActDTO getWriteOffAct() {
        return writeOffAct;
    }

    public void setWriteOffAct(WriteOffActDTO writeOffAct) {
        this.writeOffAct = writeOffAct;
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

    public UserDTO getDriver() {
        return driver;
    }

    public void setDriver(UserDTO driver) {
        this.driver = driver;
    }

    public WaybillState getState() {
        return state;
    }

    public void setState(WaybillState state) {
        this.state = state;
    }

    public UserDTO getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(UserDTO dispatcher) {
        this.dispatcher = dispatcher;
    }

    public UserDTO getManager() {
        return manager;
    }

    public void setManager(UserDTO manager) {
        this.manager = manager;
    }

    public ZonedDateTime getDateChecked() {
        return dateChecked;
    }

    public void setDateChecked(ZonedDateTime dateChecked) {
        this.dateChecked = dateChecked;
    }

    public OfferDTO getOffer() {
        return offer;
    }

    public void setOffer(OfferDTO offer) {
        this.offer = offer;
    }

    public Set<ManagedWaybillGoodsVM> getWaybillGoods() {
        return waybillGoods;
    }

    public void setWaybillGoods(Set<ManagedWaybillGoodsVM> waybillGoods) {
        this.waybillGoods = waybillGoods;
    }

//    @Override
//    public String toString () {
//        return "WaybillDTO{" +
//            "id=" + id +
//            "dispatcherId=" + dispatcher.getId() +
//            ", driverId=" + driver.getId() +
//            ", routeListId=" + routeList.getId() +
//            ", writeOffId=" + writeOffAct.getId() +
//            "}";
//    }


    @Override
    public String toString() {
        return "WaybillDTO{" +
            "id=" + id +
            ", date=" + date +
            ", routeList=" + routeList +
            ", writeOffAct=" + writeOffAct +
            ", driver=" + driver +
            ", state=" + state +
            ", dispatcher=" + dispatcher +
            ", offer=" + offer +
            '}';
    }
}
