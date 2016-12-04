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

    @Column (name = "count")
    private Long count;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "offer_id")
    private Offer offer;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private Double price;

    public OfferGoods() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
