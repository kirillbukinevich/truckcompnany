package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Goods;
import com.truckcompany.service.dto.GoodsDTO;

public class GoodsVM extends GoodsDTO {

    public GoodsVM() {
    }

    public GoodsVM(Goods goods) {
        super(goods);
    }
}
