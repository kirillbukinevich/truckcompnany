package com.truckcompany.repository;

import com.truckcompany.domain.Offer;
import com.truckcompany.domain.OfferGoods;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Viktor Dobroselsky on 02.11.2016.
 */
public interface OfferGoodsRepository extends JpaRepository <OfferGoods, Long> {

}
