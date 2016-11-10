package com.truckcompany.repository;

import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Vladimir on 08.11.2016.
 */
public interface TemplateRepository extends JpaRepository<Template, Long> {

    @Query(value = "select distinct template from Template template " +
        "left join fetch template.recipient left join fetch template.admin where template.admin = ?1")
    List<Template> findByTemplateCreatedByAdmin(User admin);

    @Query(value = "select distinct template from Template template " +
        "left join fetch template.recipient left join fetch template.admin where template.id = ?1")
    Template findOneWithRecipientAndAdmin(Long id);
}
