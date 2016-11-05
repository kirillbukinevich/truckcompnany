package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findOneById(Long id);

    List<Storage> findAll();

    List<Storage> findByCompany(Company company);

    List<Storage> findByCompanyAndActivated(Company company, boolean activated);

    @Query(value = "select distinct storage from Storage storage left join fetch storage.company where storage.id = ?1")
    Storage findByIdWithCompany(Long id);
}
