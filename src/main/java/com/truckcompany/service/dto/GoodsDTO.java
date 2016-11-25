package com.truckcompany.service.dto;

import com.truckcompany.domain.Goods;
import com.truckcompany.domain.Waybill;

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




    public GoodsDTO() {
    }

    public GoodsDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public GoodsDTO(Integer id, String name, Long uncheckedNumber, Long acceptedNumber, Long deliveredNumber, String state, WaybillDTO waybillDTO) {
        this.id = id;
        this.name = name;
        this.uncheckedNumber = uncheckedNumber;
        this.acceptedNumber = acceptedNumber;
        this.deliveredNumber = deliveredNumber;
        this.state = state;
        this.waybillDTO = waybillDTO;
    }

    public GoodsDTO(Goods goods) {
        this(goods.getId(), goods.getName());
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
