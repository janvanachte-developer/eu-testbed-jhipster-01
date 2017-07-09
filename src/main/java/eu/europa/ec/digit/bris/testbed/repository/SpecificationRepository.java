package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.Specification;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Specification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecificationRepository extends JpaRepository<Specification,Long> {
    
}
