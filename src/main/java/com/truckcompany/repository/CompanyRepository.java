package com.truckcompany.repository;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Vladimir on 20.10.2016.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /*@Query(
        value = "select distinct * from tc_company left join tc_user_company on jhi_user_id = tc_company.id \n" +
            "                         left join jhi_user on jhi_user_id = jhi_user.id \n" +
            "                         left join jhi_user_authority on jhi_user.id=user_id",
        nativeQuery = true)*/
    @Query(value = "select distinct company from Company company left join fetch company.users")
    List<Company> findAllWithUsers();
}
