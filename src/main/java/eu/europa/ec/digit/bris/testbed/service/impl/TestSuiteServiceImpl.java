package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.TestSuiteService;
import eu.europa.ec.digit.bris.testbed.domain.TestSuite;
import eu.europa.ec.digit.bris.testbed.repository.TestSuiteRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.TestSuiteSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestSuiteDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestSuiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TestSuite.
 */
@Service
@Transactional
public class TestSuiteServiceImpl implements TestSuiteService{

    private final Logger log = LoggerFactory.getLogger(TestSuiteServiceImpl.class);

    private final TestSuiteRepository testSuiteRepository;

    private final TestSuiteMapper testSuiteMapper;

    private final TestSuiteSearchRepository testSuiteSearchRepository;

    public TestSuiteServiceImpl(TestSuiteRepository testSuiteRepository, TestSuiteMapper testSuiteMapper, TestSuiteSearchRepository testSuiteSearchRepository) {
        this.testSuiteRepository = testSuiteRepository;
        this.testSuiteMapper = testSuiteMapper;
        this.testSuiteSearchRepository = testSuiteSearchRepository;
    }

    /**
     * Save a testSuite.
     *
     * @param testSuiteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TestSuiteDTO save(TestSuiteDTO testSuiteDTO) {
        log.debug("Request to save TestSuite : {}", testSuiteDTO);
        TestSuite testSuite = testSuiteMapper.toEntity(testSuiteDTO);
        testSuite = testSuiteRepository.save(testSuite);
        TestSuiteDTO result = testSuiteMapper.toDto(testSuite);
        testSuiteSearchRepository.save(testSuite);
        return result;
    }

    /**
     *  Get all the testSuites.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestSuiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestSuites");
        return testSuiteRepository.findAll(pageable)
            .map(testSuiteMapper::toDto);
    }

    /**
     *  Get one testSuite by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TestSuiteDTO findOne(Long id) {
        log.debug("Request to get TestSuite : {}", id);
        TestSuite testSuite = testSuiteRepository.findOne(id);
        return testSuiteMapper.toDto(testSuite);
    }

    /**
     *  Delete the  testSuite by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestSuite : {}", id);
        testSuiteRepository.delete(id);
        testSuiteSearchRepository.delete(id);
    }

    /**
     * Search for the testSuite corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestSuiteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TestSuites for query {}", query);
        Page<TestSuite> result = testSuiteSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(testSuiteMapper::toDto);
    }
}
