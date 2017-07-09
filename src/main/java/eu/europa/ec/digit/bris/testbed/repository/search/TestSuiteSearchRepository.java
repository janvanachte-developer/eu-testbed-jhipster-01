package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.TestSuite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TestSuite entity.
 */
public interface TestSuiteSearchRepository extends ElasticsearchRepository<TestSuite, Long> {
}
