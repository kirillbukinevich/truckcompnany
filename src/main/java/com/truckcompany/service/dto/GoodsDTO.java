package com.truckcompany.service.dto;

import com.truckcompany.domain.Goods;

/**
 * A DTO representing a goods.
 */
public class GoodsDTO {

    private Integer id;
    private String name;
    private Long uncheckedNumber;
    private Long acceptedNumber;
    private Long deliveredNumber;
    private String state;
    private WaybillDTO waybillDTO;
    private String type;
    private Double price;

    public GoodsDTO() {
    }

    public GoodsDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public GoodsDTO(Integer id, String name, Long uncheckedNumber, Long acceptedNumber, Long deliveredNumber, String state, String type, Double price) {
        this.id = id;
        this.name = name;
        this.uncheckedNumber = uncheckedNumber;
        this.acceptedNumber = acceptedNumber;
        this.deliveredNumber = deliveredNumber;
        this.state = state;
        this.type = type;
        this.price = price;
    }

    public GoodsDTO(Goods goods) {
        this(goods.getId(), goods.getName(), goods.getUncheckedNumber(), goods.getAcceptedNumber(), goods.getDeliveredNumber(), goods.getState(), goods.getType(), goods.getPrice());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public WaybillDTO getWaybillDTO() {
        return waybillDTO;
    }

    public void setWaybillDTO(WaybillDTO waybillDTO) {
        this.waybillDTO = waybillDTO;
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

    @Override
    public String toString() {
        return "GoodsDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", acceptedNumber=" + acceptedNumber +
            ", deliveredNumber=" + deliveredNumber +
            ", state='" + state + '\'' +
            ", waybillDTO=" + waybillDTO +
            '}';
    }
}
