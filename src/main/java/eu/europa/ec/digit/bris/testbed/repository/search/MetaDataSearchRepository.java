package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.MetaData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MetaData entity.
 */
public interface MetaDataSearchRepository extends ElasticsearchRepository<MetaData, Long> {
}
