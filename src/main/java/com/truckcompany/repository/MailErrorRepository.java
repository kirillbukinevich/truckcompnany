package com.truckcompany.repository;

import com.truckcompany.domain.MailError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Vladimir on 29.11.2016.
 */

public interface MailErrorRepository  extends JpaRepository<MailError, Long> {

    @Query(value = "select distinct mailerror from MailError mailerror left join fetch mailerror.template template left join fetch template.recipient")
    List<MailError> findAllWithTemplateAndRecipients();

    @Query(value = "select distinct mailerror from MailError mailerror left join fetch mailerror.template template left join fetch template.recipient where mailerror.id=?1")
    MailError findOneWithTemplateAndRecipients(Long id);
}
