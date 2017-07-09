package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.TestSuite;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestSuite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestSuiteRepository extends JpaRepository<TestSuite,Long> {
    
}
