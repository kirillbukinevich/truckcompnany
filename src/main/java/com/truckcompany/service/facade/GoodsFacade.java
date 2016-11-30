package com.truckcompany.service.facade;

import com.truckcompany.service.dto.GoodsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface GoodsFacade {
    List<GoodsDTO> findGoods(Long waybillId);

}
