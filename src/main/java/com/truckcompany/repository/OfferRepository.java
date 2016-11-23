package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Offer;
import org.springframework.boot.devtools.remote.server.Dispatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Viktor Dobroselsky on 01.11.2016.
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Page<Offer> findByCompany(Company company, Pageable pageable);
}
