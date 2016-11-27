package com.truckcompany.repository;

import com.truckcompany.domain.Goods;
import com.truckcompany.domain.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Goods entity.
 */
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Optional<Goods> findOneById(Integer goodsId);

    List<Goods> findAllByName(String name);

    @Override
    void delete(Goods goods);
}
