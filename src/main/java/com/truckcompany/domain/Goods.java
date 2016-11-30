package com.truckcompany.domain;

import com.truckcompany.domain.emb_id.WaybillGoodsId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "goods")
public class Goods implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "unchecked_number")
    private Long uncheckedNumber;

    @Column(name = "accepted_number")
    private Long acceptedNumber;

    @Column(name = "delivered_number")
    private Long deliveredNumber;

    @Column(name = "type")
    private String type;


    @Column(name = "state")
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id", nullable = false)
    private Waybill waybill;

    @Column(name = "price")
    private Double price;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getAcceptedNumber() {
        return acceptedNumber;
    }

    public void setAcceptedNumber(Long acceptedNumber) {
        this.acceptedNumber = acceptedNumber;
    }

    public Long getDeliveredNumber() {
        return deliveredNumber;
    }

    public void setDeliveredNumber(Long deliveredNumber) {
        this.deliveredNumber = deliveredNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public Long getUncheckedNumber() {
        return uncheckedNumber;
    }

    public void setUncheckedNumber(Long uncheckedNumber) {
        this.uncheckedNumber = uncheckedNumber;
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
