package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.TestCase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TestCase entity.
 */
public interface TestCaseSearchRepository extends ElasticsearchRepository<TestCase, Long> {
}
