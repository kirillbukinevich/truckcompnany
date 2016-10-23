package com.truckcompany.service.dto;

import com.truckcompany.domain.Goods;

/**
 * A DTO representing a goods.
 */
public class GoodsDTO {

    private Integer id;
    private String name;

    public GoodsDTO() {
    }

    public GoodsDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "GoodsDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
