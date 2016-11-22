package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Viktor Dobroselsky on 30.10.2016.
 */
public interface TruckRepository extends JpaRepository<Truck, Long> {

    @Query(value = "select distinct truck from Truck truck left join fetch truck.company")
    List<Truck> findAll();

    @Query(value = "select distinct truck from Truck truck left join fetch truck.company where truck.id = ?1")
    Truck getOne(Long id);

    @Query(value = "select distinct truck from Truck truck left join fetch truck.company where truck.company.id =?1")
    List<Truck> findByCompanyIdWithCompany(Long id);

    @Query(value = "select distinct truck from Truck truck left join fetch truck.company where truck.company.id =?1",
        countQuery = "select count(truck) from Truck truck left join truck.company where truck.company.id =?1")
    Page<Truck> findByCompanyIdWithCompany(Long id, Pageable pageable);

    List<Truck> findByCompany(Company company);

    @Query(value = "select distinct truck from Truck truck left join fetch truck.company where truck.company = ?1 and truck.status = 'READY'")
    List<Truck> findByCompanyAndReady(Company company);
}
