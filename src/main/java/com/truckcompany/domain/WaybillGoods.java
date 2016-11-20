package com.truckcompany.domain;


import com.truckcompany.domain.emb_id.WaybillGoodsId;
import com.truckcompany.domain.enums.WaybillGoodsState;

import javax.persistence.*;

@Entity
@Table(name = "waybill_goods")
public class WaybillGoods {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id")
    private Waybill waybill;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "goods_id")
    private Goods goods;

    @Column(name = "count")
    private Integer count;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private WaybillGoodsState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public WaybillGoodsState getState() {
        return state;
    }
    public void setState(WaybillGoodsState state) {
        this.state = state;
    }
}
