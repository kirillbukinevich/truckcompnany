package com.truckcompany.domain;

import com.truckcompany.web.rest.vm.ManagedOfferGoodsVM;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table (name = "offer_goods")
public class OfferGoods {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column (name = "id")
    private Long id;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "goods_id")
    private Goods goods;

    @Column (name = "count")
    private Integer count;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "offer_id")
    private Offer offer;

    public OfferGoods() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
