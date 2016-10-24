package com.truckcompany.repository;

import com.truckcompany.domain.WriteOffAct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WriteOffActRepository extends JpaRepository<WriteOffAct, Integer> {
    List<WriteOffAct> findAll ();

    Optional<WriteOffAct> findOneById(Integer id);
}
