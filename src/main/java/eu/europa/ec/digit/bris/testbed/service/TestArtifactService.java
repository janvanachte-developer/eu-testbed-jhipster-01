package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.TestArtifactDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TestArtifact.
 */
public interface TestArtifactService {

    /**
     * Save a testArtifact.
     *
     * @param testArtifactDTO the entity to save
     * @return the persisted entity
     */
    TestArtifactDTO save(TestArtifactDTO testArtifactDTO);

    /**
     *  Get all the testArtifacts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestArtifactDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" testArtifact.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TestArtifactDTO findOne(Long id);

    /**
     *  Delete the "id" testArtifact.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the testArtifact corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestArtifactDTO> search(String query, Pageable pageable);
}
