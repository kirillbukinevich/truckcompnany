package com.truckcompany.repository;

import com.truckcompany.domain.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Waybill entity.
 * Created by Viktor Dobroselsky.
 */
public interface WaybillRepository extends JpaRepository<Waybill, Long> {
    @Query(value = "select distinct waybill from Waybill waybill left join fetch waybill.driver " +
        "left join fetch waybill.dispatcher " +
        "left join fetch waybill.routeList " +
        "left join fetch waybill.writeOff")
    List<Waybill> findAll();

    @Query(value = "select distinct waybill from Waybill waybill left join fetch waybill.driver " +
        "left join fetch waybill.dispatcher " +
        "left join fetch waybill.routeList " +
        "left join fetch waybill.writeOff " +
        "where waybill.id = ?1")
    Optional <Waybill> findOneById(Long id);
}
