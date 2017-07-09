package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.TestArtifactService;
import eu.europa.ec.digit.bris.testbed.domain.TestArtifact;
import eu.europa.ec.digit.bris.testbed.repository.TestArtifactRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.TestArtifactSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestArtifactDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestArtifactMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TestArtifact.
 */
@Service
@Transactional
public class TestArtifactServiceImpl implements TestArtifactService{

    private final Logger log = LoggerFactory.getLogger(TestArtifactServiceImpl.class);

    private final TestArtifactRepository testArtifactRepository;

    private final TestArtifactMapper testArtifactMapper;

    private final TestArtifactSearchRepository testArtifactSearchRepository;

    public TestArtifactServiceImpl(TestArtifactRepository testArtifactRepository, TestArtifactMapper testArtifactMapper, TestArtifactSearchRepository testArtifactSearchRepository) {
        this.testArtifactRepository = testArtifactRepository;
        this.testArtifactMapper = testArtifactMapper;
        this.testArtifactSearchRepository = testArtifactSearchRepository;
    }

    /**
     * Save a testArtifact.
     *
     * @param testArtifactDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TestArtifactDTO save(TestArtifactDTO testArtifactDTO) {
        log.debug("Request to save TestArtifact : {}", testArtifactDTO);
        TestArtifact testArtifact = testArtifactMapper.toEntity(testArtifactDTO);
        testArtifact = testArtifactRepository.save(testArtifact);
        TestArtifactDTO result = testArtifactMapper.toDto(testArtifact);
        testArtifactSearchRepository.save(testArtifact);
        return result;
    }

    /**
     *  Get all the testArtifacts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestArtifactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestArtifacts");
        return testArtifactRepository.findAll(pageable)
            .map(testArtifactMapper::toDto);
    }

    /**
     *  Get one testArtifact by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TestArtifactDTO findOne(Long id) {
        log.debug("Request to get TestArtifact : {}", id);
        TestArtifact testArtifact = testArtifactRepository.findOne(id);
        return testArtifactMapper.toDto(testArtifact);
    }

    /**
     *  Delete the  testArtifact by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestArtifact : {}", id);
        testArtifactRepository.delete(id);
        testArtifactSearchRepository.delete(id);
    }

    /**
     * Search for the testArtifact corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TestArtifactDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TestArtifacts for query {}", query);
        Page<TestArtifact> result = testArtifactSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(testArtifactMapper::toDto);
    }
}
