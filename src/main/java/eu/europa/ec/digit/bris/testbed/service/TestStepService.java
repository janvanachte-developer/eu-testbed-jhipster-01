package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.TestStepDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TestStep.
 */
public interface TestStepService {

    /**
     * Save a testStep.
     *
     * @param testStepDTO the entity to save
     * @return the persisted entity
     */
    TestStepDTO save(TestStepDTO testStepDTO);

    /**
     *  Get all the testSteps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestStepDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" testStep.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TestStepDTO findOne(Long id);

    /**
     *  Delete the "id" testStep.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the testStep corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestStepDTO> search(String query, Pageable pageable);
}
