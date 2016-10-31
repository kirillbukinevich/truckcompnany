package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Truck;
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

    List<Truck> findByCompany(Company company);
}
