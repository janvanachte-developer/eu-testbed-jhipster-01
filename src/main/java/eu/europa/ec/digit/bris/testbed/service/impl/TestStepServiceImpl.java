package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.TestStepService;
import eu.europa.ec.digit.bris.testbed.domain.TestStep;
import eu.europa.ec.digit.bris.testbed.repository.TestStepRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.TestStepSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestStepDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestStepMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TestStep.
 */
@Service
@Transactional
public class TestStepServiceImpl implements TestStepService{

    private final Logger log = LoggerFactory.getLogger(TestStepServiceImpl.class);

    private final TestStepRepository testStepRepository;

    private final TestStepMapper testStepMapper;

    private final TestStepSearchRepository testStepSearchRepository;

    public TestStepServiceImpl(TestStepRepository testStepRepository, TestStepMapper testStepMapper, TestStepSearchRepository testStepSearchRepository) {
        this.testStepRepository = testStepRepository;
        this.testStepMapper = testStepMapper;
        this.testStepSearchRepository = testStepSearchRepository;
    }

    /**
     * Save a testStep.
     *
     * @param testStepDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TestStepDTO save(TestStepDTO testStepDTO) {
        log.debug("Request to save TestStep : {}", testStepDTO);
        TestStep testStep = testStepMapper.toEntity(testStepDTO);
        testStep = testStepRepository.save(testStep);
        TestStepDTO result = testStepMapper.toDto(testStep);
        testStepSearchRepository.save(testStep);
        return result;
    }

    /**
     *  Get all the testSteps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestStepDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestSteps");
        return testStepRepository.findAll(pageable)
            .map(testStepMapper::toDto);
    }

    /**
     *  Get one testStep by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TestStepDTO findOne(Long id) {
        log.debug("Request to get TestStep : {}", id);
        TestStep testStep = testStepRepository.findOne(id);
        return testStepMapper.toDto(testStep);
    }

    /**
     *  Delete the  testStep by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestStep : {}", id);
        testStepRepository.delete(id);
        testStepSearchRepository.delete(id);
    }

    /**
     * Search for the testStep corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestStepDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TestSteps for query {}", query);
        Page<TestStep> result = testStepSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(testStepMapper::toDto);
    }
}
