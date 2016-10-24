package com.truckcompany.repository;

import com.truckcompany.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {

    @Query(value = "SELECT storage from Storage storage left join fetch storage.companyId WHERE storage.id=?1 AND company_id=?2")
    Optional<Storage> findOneByIdAndCompanyIdId(Long id, Long companyId);
}
