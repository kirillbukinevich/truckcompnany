package com.truckcompany.repository.search;

import com.truckcompany.domain.Storage;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Created by Vladimir on 14.11.2016.
 */
public interface StorageSearchRepository extends SolrCrudRepository<Storage, Long> {
}
