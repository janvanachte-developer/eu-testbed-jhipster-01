package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.TestArtifact;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestArtifact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestArtifactRepository extends JpaRepository<TestArtifact,Long> {
    
}
