package com.truckcompany.service;

import com.truckcompany.domain.Goods;
import com.truckcompany.repository.GoodsRepository;
import com.truckcompany.service.dto.GoodsDTO;
import com.truckcompany.web.rest.vm.GoodsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GoodsService {

    private final Logger log = LoggerFactory.getLogger(GoodsService.class);

    @Inject
    private GoodsRepository goodsRepository;

    public Optional<Goods> getGoodsById(Integer id) {
        return goodsRepository.findOneById(id);
    }

    public Goods createGoods(String name) {
        Goods newGoods = new Goods();
        newGoods.setName(name);
        goodsRepository.save(newGoods);
        log.debug("Created Information for Goods: {}", newGoods);
        return newGoods;
    }

    public Goods createGoods(GoodsVM goodsVM) {
        Goods newGoods = new Goods();
        newGoods.setName(goodsVM.getName());
        newGoods.setUncheckedNumber(goodsVM.getUncheckedNumber());
        newGoods.setState(goodsVM.getState());

        goodsRepository.save(newGoods);
        log.debug("Created Information for Goods: {}", newGoods);
        return newGoods;
    }

    public void updateGoods(List<GoodsVM> goodsList) {
        for(GoodsVM dto: goodsList) {
            goodsRepository.findOneById(dto.getId()).ifPresent(goods -> {
                goods.setName(dto.getName());
                goods.setAcceptedNumber(dto.getAcceptedNumber());
                goods.setUncheckedNumber(dto.getUncheckedNumber());
                goods.setDeliveredNumber(dto.getDeliveredNumber());
                goods.setType(dto.getType());
                goods.setState(dto.getState());
                goods.setPrice(dto.getPrice());
                log.debug("Changed Information for Goods: {}", goods);
            });
        }
    }

    public void deleteGoods(Integer id) {
        goodsRepository.findOneById(id).ifPresent(goods -> {
            goodsRepository.delete(goods);
            log.debug("Deleted Goods: {}", goods);
        });
    }
}
