package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findOneById(Long id);

    List<Storage> findAll();

    @Query( value = "select distinct count(storage) from Storage storage left join storage.company where storage.company = ?1 and storage.deleted = false")
    Long countStoragesBelongsCompanyAndNotDeleted(Company company);

    @Query( value = "select distinct storage from Storage storage left join fetch storage.company where storage.company =?1 and storage.deleted = false",
            countQuery = "select distinct count(storage) from Storage storage left join storage.company where storage.company =?1 and storage.deleted = false")
    Page<Storage> findByCompanyAndNotDeleted(Company company, Pageable page);

    @Query( value = "select distinct count(storage) from Storage storage left join storage.company where storage.company = ?1 and storage.deleted = false and storage.activated = true")
    Long countStoragesBelongCompanyAndNotDeletedAndActivated(Company company);

    @Query( value = "select distinct storage from Storage storage left join fetch storage.company where storage.company =?1 and storage.deleted = false and storage.activated = true",
        countQuery = "select distinct count(storage) from Storage storage left join storage.company where storage.company =?1 and storage.deleted = false and storage.activated = true")
    Page<Storage> findByCompanyAndNotDeletedAndActivated(Company company, Pageable page);


    Page<Storage> findByCompany(Company company, Pageable page);

    Page<Storage> findByCompanyAndActivated(Company company, boolean activated, Pageable page);

    @Query(value = "select distinct storage from Storage storage left join fetch storage.company where storage.id = ?1")
    Storage findByIdWithCompany(Long id);

    List<Storage> findByCompany(Company company);
}
