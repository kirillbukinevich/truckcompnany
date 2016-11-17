package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.WriteOffAct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Viktor Dobroselsky.
 */
public interface WriteOffActRepository extends JpaRepository<WriteOffAct, Long> {
    List<WriteOffAct> findAll ();

    Optional<WriteOffAct> findOneById(Long id);

    List<WriteOffAct> findByCompany(Company company);
}
