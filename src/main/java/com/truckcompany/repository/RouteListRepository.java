package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.RouteList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface RouteListRepository extends JpaRepository <RouteList, Long> {
    @Query(value = "select distinct routeList from RouteList routeList left join fetch routeList.truck " +
        "left join fetch routeList.leavingStorage " +
        "left join fetch routeList.arrivalStorage")
    List<RouteList> findAll();

    @Query(value = "select distinct routeList from RouteList routeList " +
        "left join fetch routeList.truck " +
        "left join fetch routeList.leavingStorage " +
        "left join fetch routeList.arrivalStorage " +
        "where routeList.id = ?1")
    Optional<RouteList> findOneById (Long id);

    @Query(value = "select distinct routeList from RouteList routeList " +
        "left join fetch routeList.truck " +
        "left join fetch routeList.leavingStorage " +
        "left join fetch routeList.arrivalStorage " +
        "where routeList.id = ?1")
    @Override
    RouteList findOne (Long id);

    Optional<List<RouteList>> findByCompany(Company company);

    Optional<List<RouteList>> findByCompanyAndCreationDateBetween(Company company, ZonedDateTime fromDate,
                                                                  ZonedDateTime toDate);

    Page<RouteList> findPageByCompany(Company company, Pageable pageable);

    Page<RouteList> findPageByCompanyAndCreationDateBetween(Company company, ZonedDateTime fromDate, ZonedDateTime toDate,
                                                            Pageable pageable);

    @Query(value = "select distinct routeList from RouteList routeList where " +
        //"routeList.company = ?1 and routeList.leavingDate > ?2 and routeList.leavingDate < ?2 or " +
        "routeList.company = ?1 and routeList.leavingDate between ?2 and ?3 or " +
        "routeList.company = ?1 and routeList.arrivalDate between ?2 and ?3 or " +
        "routeList.company = ?1 and routeList.leavingDate > ?2 and routeList.arrivalDate < ?3")
    List<RouteList> findRouteListsByDate(Company company, ZonedDateTime start, ZonedDateTime end);
}
