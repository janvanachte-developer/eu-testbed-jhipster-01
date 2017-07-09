package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.TestCaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TestCase.
 */
public interface TestCaseService {

    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save
     * @return the persisted entity
     */
    TestCaseDTO save(TestCaseDTO testCaseDTO);

    /**
     *  Get all the testCases.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestCaseDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" testCase.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TestCaseDTO findOne(Long id);

    /**
     *  Delete the "id" testCase.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the testCase corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TestCaseDTO> search(String query, Pageable pageable);
}
