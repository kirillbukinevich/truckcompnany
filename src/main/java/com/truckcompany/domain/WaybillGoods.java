package com.truckcompany.domain;


import com.truckcompany.domain.emb_id.WaybillGoodsId;
import com.truckcompany.domain.enums.WaybillGoodsState;

import javax.persistence.*;

@Entity
@Table(name = "waybill_goods")
public class WaybillGoods {
    @EmbeddedId
    private WaybillGoodsId waybillGoodsId;

    @Column(name = "count")
    private String count;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private WaybillGoodsState state;

    public WaybillGoodsId getWaybillGoodsId() {
        return waybillGoodsId;
    }
    public void setWaybillGoodsId(WaybillGoodsId waybillGoodsId) {
        this.waybillGoodsId = waybillGoodsId;
    }

    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }

    public WaybillGoodsState getState() {
        return state;
    }
    public void setState(WaybillGoodsState state) {
        this.state = state;
    }
}
