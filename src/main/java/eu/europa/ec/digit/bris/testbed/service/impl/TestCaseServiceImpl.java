package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.TestCaseService;
import eu.europa.ec.digit.bris.testbed.domain.TestCase;
import eu.europa.ec.digit.bris.testbed.repository.TestCaseRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.TestCaseSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestCaseDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestCaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TestCase.
 */
@Service
@Transactional
public class TestCaseServiceImpl implements TestCaseService{

    private final Logger log = LoggerFactory.getLogger(TestCaseServiceImpl.class);

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    private final TestCaseSearchRepository testCaseSearchRepository;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper, TestCaseSearchRepository testCaseSearchRepository) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
        this.testCaseSearchRepository = testCaseSearchRepository;
    }

    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TestCaseDTO save(TestCaseDTO testCaseDTO) {
        log.debug("Request to save TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        TestCaseDTO result = testCaseMapper.toDto(testCase);
        testCaseSearchRepository.save(testCase);
        return result;
    }

    /**
     *  Get all the testCases.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestCaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestCases");
        return testCaseRepository.findAll(pageable)
            .map(testCaseMapper::toDto);
    }

    /**
     *  Get one testCase by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TestCaseDTO findOne(Long id) {
        log.debug("Request to get TestCase : {}", id);
        TestCase testCase = testCaseRepository.findOne(id);
        return testCaseMapper.toDto(testCase);
    }

    /**
     *  Delete the  testCase by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestCase : {}", id);
        testCaseRepository.delete(id);
        testCaseSearchRepository.delete(id);
    }

    /**
     * Search for the testCase corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestCaseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TestCases for query {}", query);
        Page<TestCase> result = testCaseSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(testCaseMapper::toDto);
    }
}
