package com.truckcompany.repository.search;

import com.truckcompany.domain.WaybillIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import static com.truckcompany.repository.search.SearchableWaybillDefinition.*;

/**
 * Created by Dmitry on 11.12.2016.
 */
public interface WaybillSearchRepository extends SolrCrudRepository<WaybillIndex, Long> {

    @Highlight(prefix = "<b>", postfix = "</b>")
    @Query(
        value = "(number:?0 OR dispatcherFirstName:?0 OR dispatcherLastName:?0 " +
                "OR driverFirstName:?0 OR driverLastName:?0 OR state:?0) AND companyId:?1",
        fields = {ID_FIELD_NAME, COMPANY_ID_FIELD_NAME, NUMBER_FIELD_NAME, DATE_FIELD_NAME, DISPATCHER_FIRST_NAME_FIELD_NAME,
            DISPATCHER_LAST_NAME_FIELD_NAME, DRIVER_FIRST_NAME_FIELD_NAME, DRIVER_LAST_NAME_FIELD_NAME, STATE_FIELD_NAME},
        defaultOperator = org.springframework.data.solr.core.query.Query.Operator.OR)
    HighlightPage<WaybillIndex> findByAllFieldsAndCompanyId(String query, Long CompanyId, Pageable page);
}
