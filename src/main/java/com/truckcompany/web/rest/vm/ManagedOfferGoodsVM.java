package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.OfferGoods;

public class ManagedOfferGoodsVM {
    private Long id;

    private String name;

    private Long count;

    private Long offerId;

    private String type;

    private Double price;

    public ManagedOfferGoodsVM() {
    }

    public ManagedOfferGoodsVM(Long id, String name, Long count, Long offerId) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.offerId = offerId;
    }

    public ManagedOfferGoodsVM (OfferGoods offerGoods) {
        this.id = offerGoods.getId();
        this.name = offerGoods.getName();
        this.count = offerGoods.getCount();
        this.offerId = offerGoods.getOffer().getId();
        this.type = offerGoods.getType();
        this.price = offerGoods.getPrice();
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
