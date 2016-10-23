package com.truckcompany.service.mapper;

import com.truckcompany.domain.Goods;
import com.truckcompany.service.dto.GoodsDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Goods and its DTO GoodsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GoodsMapper {

    GoodsDTO goodsToGoodsDTO(Goods goods);

    List<GoodsDTO> goodsToGoodsDTOs(List<Goods> goodsList);

    List<Goods> goodsDTOsToGoods(List<GoodsDTO> goodsDTOs);

    Goods goodsDTOToGoods(GoodsDTO goodsDTO);

}
