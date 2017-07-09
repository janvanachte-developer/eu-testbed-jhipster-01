package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.TestStep;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestStepRepository extends JpaRepository<TestStep,Long> {
    
}
