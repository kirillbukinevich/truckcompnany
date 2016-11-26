package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.RouteList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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

    Page<RouteList> findPageByCompany(Company company, Pageable pageable);

    Page<RouteList> findPageByCompanyAndCreationDateBetween(Company company, ZonedDateTime fromDate, ZonedDateTime toDate,
                                                            Pageable pageable);
}
