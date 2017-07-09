package eu.europa.ec.digit.bris.testbed.web.rest;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import eu.europa.ec.digit.bris.testbed.domain.TestStep;
import eu.europa.ec.digit.bris.testbed.repository.TestStepRepository;
import eu.europa.ec.digit.bris.testbed.service.TestStepService;
import eu.europa.ec.digit.bris.testbed.repository.search.TestStepSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestStepDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestStepMapper;
import eu.europa.ec.digit.bris.testbed.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TestStepResource REST controller.
 *
 * @see TestStepResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestbedApp.class)
public class TestStepResourceIntTest {

    @Autowired
    private TestStepRepository testStepRepository;

    @Autowired
    private TestStepMapper testStepMapper;

    @Autowired
    private TestStepService testStepService;

    @Autowired
    private TestStepSearchRepository testStepSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestStepMockMvc;

    private TestStep testStep;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestStepResource testStepResource = new TestStepResource(testStepService);
        this.restTestStepMockMvc = MockMvcBuilders.standaloneSetup(testStepResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestStep createEntity(EntityManager em) {
        TestStep testStep = new TestStep();
        return testStep;
    }

    @Before
    public void initTest() {
        testStepSearchRepository.deleteAll();
        testStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestStep() throws Exception {
        int databaseSizeBeforeCreate = testStepRepository.findAll().size();

        // Create the TestStep
        TestStepDTO testStepDTO = testStepMapper.toDto(testStep);
        restTestStepMockMvc.perform(post("/api/test-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testStepDTO)))
            .andExpect(status().isCreated());

        // Validate the TestStep in the database
        List<TestStep> testStepList = testStepRepository.findAll();
        assertThat(testStepList).hasSize(databaseSizeBeforeCreate + 1);
        TestStep testTestStep = testStepList.get(testStepList.size() - 1);

        // Validate the TestStep in Elasticsearch
        TestStep testStepEs = testStepSearchRepository.findOne(testTestStep.getId());
        assertThat(testStepEs).isEqualToComparingFieldByField(testTestStep);
    }

    @Test
    @Transactional
    public void createTestStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testStepRepository.findAll().size();

        // Create the TestStep with an existing ID
        testStep.setId(1L);
        TestStepDTO testStepDTO = testStepMapper.toDto(testStep);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestStepMockMvc.perform(post("/api/test-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testStepDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TestStep> testStepList = testStepRepository.findAll();
        assertThat(testStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestSteps() throws Exception {
        // Initialize the database
        testStepRepository.saveAndFlush(testStep);

        // Get all the testStepList
        restTestStepMockMvc.perform(get("/api/test-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testStep.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTestStep() throws Exception {
        // Initialize the database
        testStepRepository.saveAndFlush(testStep);

        // Get the testStep
        restTestStepMockMvc.perform(get("/api/test-steps/{id}", testStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testStep.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTestStep() throws Exception {
        // Get the testStep
        restTestStepMockMvc.perform(get("/api/test-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestStep() throws Exception {
        // Initialize the database
        testStepRepository.saveAndFlush(testStep);
        testStepSearchRepository.save(testStep);
        int databaseSizeBeforeUpdate = testStepRepository.findAll().size();

        // Update the testStep
        TestStep updatedTestStep = testStepRepository.findOne(testStep.getId());
        TestStepDTO testStepDTO = testStepMapper.toDto(updatedTestStep);

        restTestStepMockMvc.perform(put("/api/test-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testStepDTO)))
            .andExpect(status().isOk());

        // Validate the TestStep in the database
        List<TestStep> testStepList = testStepRepository.findAll();
        assertThat(testStepList).hasSize(databaseSizeBeforeUpdate);
        TestStep testTestStep = testStepList.get(testStepList.size() - 1);

        // Validate the TestStep in Elasticsearch
        TestStep testStepEs = testStepSearchRepository.findOne(testTestStep.getId());
        assertThat(testStepEs).isEqualToComparingFieldByField(testTestStep);
    }

    @Test
    @Transactional
    public void updateNonExistingTestStep() throws Exception {
        int databaseSizeBeforeUpdate = testStepRepository.findAll().size();

        // Create the TestStep
        TestStepDTO testStepDTO = testStepMapper.toDto(testStep);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestStepMockMvc.perform(put("/api/test-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testStepDTO)))
            .andExpect(status().isCreated());

        // Validate the TestStep in the database
        List<TestStep> testStepList = testStepRepository.findAll();
        assertThat(testStepList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestStep() throws Exception {
        // Initialize the database
        testStepRepository.saveAndFlush(testStep);
        testStepSearchRepository.save(testStep);
        int databaseSizeBeforeDelete = testStepRepository.findAll().size();

        // Get the testStep
        restTestStepMockMvc.perform(delete("/api/test-steps/{id}", testStep.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean testStepExistsInEs = testStepSearchRepository.exists(testStep.getId());
        assertThat(testStepExistsInEs).isFalse();

        // Validate the database is empty
        List<TestStep> testStepList = testStepRepository.findAll();
        assertThat(testStepList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTestStep() throws Exception {
        // Initialize the database
        testStepRepository.saveAndFlush(testStep);
        testStepSearchRepository.save(testStep);

        // Search the testStep
        restTestStepMockMvc.perform(get("/api/_search/test-steps?query=id:" + testStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testStep.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestStep.class);
        TestStep testStep1 = new TestStep();
        testStep1.setId(1L);
        TestStep testStep2 = new TestStep();
        testStep2.setId(testStep1.getId());
        assertThat(testStep1).isEqualTo(testStep2);
        testStep2.setId(2L);
        assertThat(testStep1).isNotEqualTo(testStep2);
        testStep1.setId(null);
        assertThat(testStep1).isNotEqualTo(testStep2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestStepDTO.class);
        TestStepDTO testStepDTO1 = new TestStepDTO();
        testStepDTO1.setId(1L);
        TestStepDTO testStepDTO2 = new TestStepDTO();
        assertThat(testStepDTO1).isNotEqualTo(testStepDTO2);
        testStepDTO2.setId(testStepDTO1.getId());
        assertThat(testStepDTO1).isEqualTo(testStepDTO2);
        testStepDTO2.setId(2L);
        assertThat(testStepDTO1).isNotEqualTo(testStepDTO2);
        testStepDTO1.setId(null);
        assertThat(testStepDTO1).isNotEqualTo(testStepDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(testStepMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(testStepMapper.fromId(null)).isNull();
    }
}
