package com.truckcompany.repository;

import com.truckcompany.domain.Authority;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);


    @Query(value = "select distinct user from User user left join fetch user.authorities left join fetch user.company where user.login=?1")
    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);

    @Query(value = "select distinct user from User user left join fetch user.authorities",
        countQuery = "select count(user) from User user")
    Page<User> findAllWithAuthorities(Pageable pageable);

    @Override
    void delete(User t);

    @Query(value = "select distinct user from User user left join fetch user.authorities where user.company.id = ?1")
    List<User> findUsersBelongCompanyWithAuthorities(Long id);

    @Query(value = "select distinct user from User user left join user.authorities auth where user.company.id = ?1 and auth.name <> 'ROLE_ADMIN'",
           countQuery ="select distinct count(user) from User user left join user.authorities auth where user.company.id = ?1 and auth.name <> 'ROLE_ADMIN'")
    Page<User> findUsersBelongsCompanyWithoutAdmin(Long idCompany, Pageable page);

    @Query(value = "select distinct user from User user left join user.authorities auth where user.company.id = ?1 and auth.name <> 'ROLE_ADMIN'")
    List<User> findUsersBelongsCompanyWithoutAdmin(Long idCompany);

    @Query( value = "select distinct count(user) from User user left join user.authorities auth where user.company.id = ?1 and auth.name <> 'ROLE_ADMIN'")
    Long countUsersBelongsCompanyWithoutAdmin(Long idCompany);

    List<User> findByCompanyAndAuthorities(Company company, Set authorities);


}
