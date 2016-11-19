package com.truckcompany.domain.emb_id;


import com.truckcompany.domain.Goods;
import com.truckcompany.domain.Waybill;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class WaybillGoodsId implements Serializable{
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "waybill_id")
    private Waybill waybill;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "goods_id")
    private Goods goods;

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
