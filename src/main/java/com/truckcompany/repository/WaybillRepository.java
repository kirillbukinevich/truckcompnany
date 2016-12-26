package com.truckcompany.repository;


import com.truckcompany.domain.*;

import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;

import com.truckcompany.domain.enums.WaybillState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Waybill entity.
 * Created by Viktor Dobroselsky.
 */
public interface WaybillRepository extends JpaRepository<Waybill, Long> {
    @Query(value = "select distinct waybill from Waybill waybill left join fetch waybill.driver " +
        "left join fetch waybill.dispatcher " +
        "left join fetch waybill.manager " +
        "left join fetch waybill.goods " +
        "left join fetch waybill.routeList ")
    List<Waybill> findAll();

    @Query(value = "select distinct waybill from Waybill waybill left join fetch waybill.driver " +
        "left join fetch waybill.dispatcher " +
        "left join fetch waybill.manager " +
        "left join fetch waybill.goods " +
        "left join fetch waybill.routeList " +
        "where waybill.id = ?1")
    Optional <Waybill> findOneById(Long id);

    @Query(value = "select waybill from Waybill as waybill " +
        "left join fetch waybill.driver " +
        "left join fetch waybill.routeList " +
        "where waybill.driver=?1 AND " +
        "waybill.routeList.leavingDate = (select min(routeList.leavingDate) from RouteList as routeList  " +
        "where routeList.state='TRANSPORTATION')")
    Optional<Waybill> findOneByDriver(User driver);

    @Query(value = "select waybill from Waybill as waybill " +
        "left join fetch waybill.driver " +
        "left join fetch waybill.routeList " +
        "where waybill.driver=?1 AND waybill.state ='DELIVERED'",
        countQuery = "select count(waybill) from Waybill waybill " +
            "left join waybill.driver  " +
            "where waybill.driver=?1 AND waybill.state ='DELIVERED'")
    Page<Waybill> findPageHistoryByDriver(User driver,Pageable pageable);

    @Query(value = "select waybill from Waybill as waybill " +
        "left join fetch waybill.driver " +
        "left join fetch waybill.routeList " +
        "where waybill.driver=?1 AND waybill.state ='CHECKED'",
        countQuery = "select count(waybill) from Waybill waybill " +
            "left join waybill.driver  " +
            "where waybill.driver=?1 AND waybill.state ='CHECKED'")
    Page<Waybill> findPageTimetableByDriver(User driver,Pageable pageable);



    List<Waybill> findByCompany(Company company);

    List<Waybill> findByCompanyAndRouteListCreationDateBetween(Company company,
                                                               ZonedDateTime fromDate, ZonedDateTime toDate);

    List<Waybill> findByCompanyAndState(Company company, WaybillState state);

    List<Waybill> findByCompanyAndStateAndRouteListArrivalDateBetween(Company company, WaybillState state,
                                                      ZonedDateTime fromDate, ZonedDateTime toDate);

    Page<Waybill> findPageByCompany(Company company, Pageable pageable);

    Page<Waybill> findPageByCompanyAndState(Company company, WaybillState state, Pageable pageable);

    Optional<Waybill> findByRouteList(RouteList routeList);

    Page<Waybill> findPageByDispatcher(User dispatcher, Pageable pageable);


    Page<Waybill> findPageByCompanyAndRouteListCreationDateBetween(Company company, ZonedDateTime fromDate, ZonedDateTime toDate, Pageable pageable);
}
