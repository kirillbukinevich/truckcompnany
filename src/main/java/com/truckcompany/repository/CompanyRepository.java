package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vladimir on 20.10.2016.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
