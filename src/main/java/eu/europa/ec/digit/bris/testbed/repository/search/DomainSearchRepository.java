package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.Domain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Domain entity.
 */
public interface DomainSearchRepository extends ElasticsearchRepository<Domain, Long> {
}
