package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.WaybillGoods;
import com.truckcompany.domain.enums.WaybillGoodsState;

public class ManagedWaybillGoodsVM {
    private Long id;

    private String name;

    private WaybillGoodsState state;

    private Integer count;

    public ManagedWaybillGoodsVM(Long id, String name, WaybillGoodsState state, Integer count) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.count = count;
    }

    public ManagedWaybillGoodsVM() {
    }

    public ManagedWaybillGoodsVM(WaybillGoods waybillGoods) {
        this.id = waybillGoods.getId();
        this.name = waybillGoods.getGoods().getName();
        this.count = waybillGoods.getCount();
        this.state = waybillGoods.getState();
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

    public WaybillGoodsState getState() {
        return state;
    }

    public void setState(WaybillGoodsState state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
