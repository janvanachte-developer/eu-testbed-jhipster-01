package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.TestSuiteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TestSuite.
 */
public interface TestSuiteService {

    /**
     * Save a testSuite.
     *
     * @param testSuiteDTO the entity to save
     * @return the persisted entity
     */
    TestSuiteDTO save(TestSuiteDTO testSuiteDTO);

    /**
     *  Get all the testSuites.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestSuiteDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" testSuite.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TestSuiteDTO findOne(Long id);

    /**
     *  Delete the "id" testSuite.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the testSuite corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestSuiteDTO> search(String query, Pageable pageable);
}
