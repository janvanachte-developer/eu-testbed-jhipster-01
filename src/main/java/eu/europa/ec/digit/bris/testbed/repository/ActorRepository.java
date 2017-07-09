package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.Actor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Actor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActorRepository extends JpaRepository<Actor,Long> {
    
}
