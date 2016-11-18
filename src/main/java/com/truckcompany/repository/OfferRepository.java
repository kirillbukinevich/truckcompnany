package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Viktor Dobroselsky on 01.11.2016.
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByCompany(Company company);
}
