package com.truckcompany.service.dto;

import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.enums.WaybillState;

import java.time.ZonedDateTime;

/**
 * A DTO representing a Waybill.
 * Created by Viktor Dobroselsky.
 */
public class WaybillDTO {
    private Long id;

    private ZonedDateTime date;

    private UserDTO driver;

    private WaybillState state;

    private UserDTO dispatcher;

    private UserDTO manager;

    private ZonedDateTime dateChecked;

    public WaybillDTO(Long id, ZonedDateTime date, UserDTO driver, WaybillState state, UserDTO dispatcher, UserDTO manager, ZonedDateTime dateChecked) {
        this.id = id;
        this.date = date;
        this.driver = driver;
        this.state = state;
        this.dispatcher = dispatcher;
        this.manager = manager;
        this.dateChecked = dateChecked;
    }

    public WaybillDTO() {
    }

    public WaybillDTO(Waybill waybill) {
        this.id = waybill.getId();
        this.date = waybill.getDate();
        this.driver = new UserDTO(waybill.getDriver());
        this.dispatcher = new UserDTO(waybill.getDispatcher());
        this.state = waybill.getState();
        this.manager = waybill.getManager() == null ? null : new UserDTO(waybill.getManager());
        this.dateChecked = waybill.getDateChecked();
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
}
