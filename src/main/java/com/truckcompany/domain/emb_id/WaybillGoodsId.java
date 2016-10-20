package com.truckcompany.domain.emb_id;


import com.truckcompany.domain.Goods;
import com.truckcompany.domain.Waybill;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class WaybillGoodsId implements Serializable{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id", nullable = false)
    private Waybill waybillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goodsId;

    public Waybill getWaybillId() {
        return waybillId;
    }
    public void setWaybillId(Waybill waybillId) {
        this.waybillId = waybillId;
    }

    public Goods getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Goods goodsId) {
        this.goodsId = goodsId;
    }
}
