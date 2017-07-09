package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.MetaData;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaDataRepository extends JpaRepository<MetaData,Long> {
    
}
