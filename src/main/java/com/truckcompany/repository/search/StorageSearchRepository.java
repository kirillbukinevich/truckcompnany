package com.truckcompany.repository.search;

import com.truckcompany.domain.StorageIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import static com.truckcompany.repository.search.SearchableStorageDefinition.*;

/**
 * Created by Vladimir on 14.11.2016.
 */
public interface StorageSearchRepository extends SolrCrudRepository<StorageIndex, Long> {

    @Highlight(prefix = "<b>", postfix = "</b>")
    @Query(fields = {ID_FIELD_NAME, NAME_FIELD_NAME, ACTIVATED_FIELD_NAME})
     HighlightPage<StorageIndex> findByNameOrAddressIn(String name, String address, Pageable page);

    @Highlight(prefix = "<b>", postfix = "</b>")
    @Query(fields = {ID_FIELD_NAME, NAME_FIELD_NAME, ACTIVATED_FIELD_NAME})
    HighlightPage<StorageIndex> findByNameOrAddressStartsWithAndIdCompany(String name, String address, Long id, Pageable page);


    @Highlight(prefix = "<b>", postfix = "</b>")
    @Query(
        value = "(name:?0 OR address:?0) AND idcompany:?1 AND deleted: false",
        fields = {ID_FIELD_NAME, NAME_FIELD_NAME, ACTIVATED_FIELD_NAME, ADDRESS_FIELD_NAME },
        defaultOperator = org.springframework.data.solr.core.query.Query.Operator.OR)
    HighlightPage<StorageIndex> findByNameLikeOrAddressLikeAndIdCompany(String query, Long idCompany, Pageable page);





}
