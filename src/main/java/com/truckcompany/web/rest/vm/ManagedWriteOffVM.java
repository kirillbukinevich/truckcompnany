package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.WriteOffAct;

import java.time.ZonedDateTime;

public class ManagedWriteOffVM {
    private Long id;

    private ZonedDateTime date;

    private Integer count;

    public ManagedWriteOffVM() {
    }

    public ManagedWriteOffVM(Long id, ZonedDateTime date, Integer count) {
        this.id = id;
        this.date = date;
        this.count = count;
    }

    public ManagedWriteOffVM (WriteOffAct writeOff) {
        this.id = writeOff.getId();
        this.date = writeOff.getDate();
        this.count = writeOff.getCount();
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

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
}
