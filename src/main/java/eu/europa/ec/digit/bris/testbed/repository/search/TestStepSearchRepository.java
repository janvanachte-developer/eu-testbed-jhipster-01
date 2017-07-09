package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.TestStep;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TestStep entity.
 */
public interface TestStepSearchRepository extends ElasticsearchRepository<TestStep, Long> {
}
