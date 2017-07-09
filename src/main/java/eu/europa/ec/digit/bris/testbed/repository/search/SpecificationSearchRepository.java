package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.Specification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Specification entity.
 */
public interface SpecificationSearchRepository extends ElasticsearchRepository<Specification, Long> {
}
