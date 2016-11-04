package com.truckcompany.domain;

import com.truckcompany.web.rest.vm.ManagedOfferGoodsVM;

import javax.persistence.*;

@Entity
@Table (name = "offer_goods")
public class OfferGoods {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column (name = "id")
    private Long id;

    @Column (name = "name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
