package eu.europa.ec.digit.bris.testbed.repository.search;

import eu.europa.ec.digit.bris.testbed.domain.TestArtifact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TestArtifact entity.
 */
public interface TestArtifactSearchRepository extends ElasticsearchRepository<TestArtifact, Long> {
}
