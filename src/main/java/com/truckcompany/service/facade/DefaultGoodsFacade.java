package com.truckcompany.service.facade;

import com.truckcompany.domain.Goods;
import com.truckcompany.service.GoodsService;
import com.truckcompany.service.dto.GoodsDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;


@Component
public class DefaultGoodsFacade implements GoodsFacade{

    @Inject
    GoodsService goodsService;

    @Override
    public List<GoodsDTO> findGoods(Long waybillId) {

        Function<Goods, GoodsDTO> toDTO = convertToGoodDto();

        List<Goods> goodsList = goodsService.getGoodsByWaybill(waybillId);
        return (goodsList.stream().map(toDTO).collect(toList()));

    }

    private Function<Goods, GoodsDTO> convertToGoodDto() {
        return GoodsDTO::new;
    }

}
