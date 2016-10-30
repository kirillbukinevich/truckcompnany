package com.truckcompany.repository;

import com.truckcompany.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {


    Optional<Storage> findOneById(Long id);

    List<Storage> findAll();
}
