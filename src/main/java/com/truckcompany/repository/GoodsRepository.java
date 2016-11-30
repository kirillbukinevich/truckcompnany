package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Goods;
import com.truckcompany.domain.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Goods entity.
 */
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Optional<Goods> findOneById(Integer goodsId);

    List<Goods> findAllByName(String name);

    @Query(value = "select distinct goods from Goods goods left join fetch goods.waybill where goods.waybill.id = ?1")
    List<Goods> findByWaybillId(Long waybillId);


    @Override
    void delete(Goods goods);
}
