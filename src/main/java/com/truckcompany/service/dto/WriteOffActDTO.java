package com.truckcompany.service.dto;

import com.truckcompany.domain.WriteOffAct;

import java.time.ZonedDateTime;

public class WriteOffActDTO {
    private Long id;

    private Integer count;

    private ZonedDateTime date;

    public WriteOffActDTO(Long id, Integer count, ZonedDateTime date){
        this.id = id;
        this.count = count;
        this.date = date;
    }

    public WriteOffActDTO(WriteOffAct writeOffAct){
        this(
            writeOffAct.getId(),
            writeOffAct.getCount(),
            writeOffAct.getDate()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
