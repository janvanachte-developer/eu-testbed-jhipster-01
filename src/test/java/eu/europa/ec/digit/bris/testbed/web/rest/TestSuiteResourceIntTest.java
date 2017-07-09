package eu.europa.ec.digit.bris.testbed.web.rest;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import eu.europa.ec.digit.bris.testbed.domain.TestSuite;
import eu.europa.ec.digit.bris.testbed.repository.TestSuiteRepository;
import eu.europa.ec.digit.bris.testbed.service.TestSuiteService;
import eu.europa.ec.digit.bris.testbed.repository.search.TestSuiteSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestSuiteDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestSuiteMapper;
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
 * Test class for the TestSuiteResource REST controller.
 *
 * @see TestSuiteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestbedApp.class)
public class TestSuiteResourceIntTest {

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private TestSuiteMapper testSuiteMapper;

    @Autowired
    private TestSuiteService testSuiteService;

    @Autowired
    private TestSuiteSearchRepository testSuiteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestSuiteMockMvc;

    private TestSuite testSuite;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestSuiteResource testSuiteResource = new TestSuiteResource(testSuiteService);
        this.restTestSuiteMockMvc = MockMvcBuilders.standaloneSetup(testSuiteResource)
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
    public static TestSuite createEntity(EntityManager em) {
        TestSuite testSuite = new TestSuite();
        return testSuite;
    }

    @Before
    public void initTest() {
        testSuiteSearchRepository.deleteAll();
        testSuite = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestSuite() throws Exception {
        int databaseSizeBeforeCreate = testSuiteRepository.findAll().size();

        // Create the TestSuite
        TestSuiteDTO testSuiteDTO = testSuiteMapper.toDto(testSuite);
        restTestSuiteMockMvc.perform(post("/api/test-suites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testSuiteDTO)))
            .andExpect(status().isCreated());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeCreate + 1);
        TestSuite testTestSuite = testSuiteList.get(testSuiteList.size() - 1);

        // Validate the TestSuite in Elasticsearch
        TestSuite testSuiteEs = testSuiteSearchRepository.findOne(testTestSuite.getId());
        assertThat(testSuiteEs).isEqualToComparingFieldByField(testTestSuite);
    }

    @Test
    @Transactional
    public void createTestSuiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testSuiteRepository.findAll().size();

        // Create the TestSuite with an existing ID
        testSuite.setId(1L);
        TestSuiteDTO testSuiteDTO = testSuiteMapper.toDto(testSuite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestSuiteMockMvc.perform(post("/api/test-suites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testSuiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestSuites() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get all the testSuiteList
        restTestSuiteMockMvc.perform(get("/api/test-suites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testSuite.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);

        // Get the testSuite
        restTestSuiteMockMvc.perform(get("/api/test-suites/{id}", testSuite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testSuite.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTestSuite() throws Exception {
        // Get the testSuite
        restTestSuiteMockMvc.perform(get("/api/test-suites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);
        testSuiteSearchRepository.save(testSuite);
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();

        // Update the testSuite
        TestSuite updatedTestSuite = testSuiteRepository.findOne(testSuite.getId());
        TestSuiteDTO testSuiteDTO = testSuiteMapper.toDto(updatedTestSuite);

        restTestSuiteMockMvc.perform(put("/api/test-suites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testSuiteDTO)))
            .andExpect(status().isOk());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate);
        TestSuite testTestSuite = testSuiteList.get(testSuiteList.size() - 1);

        // Validate the TestSuite in Elasticsearch
        TestSuite testSuiteEs = testSuiteSearchRepository.findOne(testTestSuite.getId());
        assertThat(testSuiteEs).isEqualToComparingFieldByField(testTestSuite);
    }

    @Test
    @Transactional
    public void updateNonExistingTestSuite() throws Exception {
        int databaseSizeBeforeUpdate = testSuiteRepository.findAll().size();

        // Create the TestSuite
        TestSuiteDTO testSuiteDTO = testSuiteMapper.toDto(testSuite);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestSuiteMockMvc.perform(put("/api/test-suites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testSuiteDTO)))
            .andExpect(status().isCreated());

        // Validate the TestSuite in the database
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);
        testSuiteSearchRepository.save(testSuite);
        int databaseSizeBeforeDelete = testSuiteRepository.findAll().size();

        // Get the testSuite
        restTestSuiteMockMvc.perform(delete("/api/test-suites/{id}", testSuite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean testSuiteExistsInEs = testSuiteSearchRepository.exists(testSuite.getId());
        assertThat(testSuiteExistsInEs).isFalse();

        // Validate the database is empty
        List<TestSuite> testSuiteList = testSuiteRepository.findAll();
        assertThat(testSuiteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTestSuite() throws Exception {
        // Initialize the database
        testSuiteRepository.saveAndFlush(testSuite);
        testSuiteSearchRepository.save(testSuite);

        // Search the testSuite
        restTestSuiteMockMvc.perform(get("/api/_search/test-suites?query=id:" + testSuite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testSuite.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestSuite.class);
        TestSuite testSuite1 = new TestSuite();
        testSuite1.setId(1L);
        TestSuite testSuite2 = new TestSuite();
        testSuite2.setId(testSuite1.getId());
        assertThat(testSuite1).isEqualTo(testSuite2);
        testSuite2.setId(2L);
        assertThat(testSuite1).isNotEqualTo(testSuite2);
        testSuite1.setId(null);
        assertThat(testSuite1).isNotEqualTo(testSuite2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestSuiteDTO.class);
        TestSuiteDTO testSuiteDTO1 = new TestSuiteDTO();
        testSuiteDTO1.setId(1L);
        TestSuiteDTO testSuiteDTO2 = new TestSuiteDTO();
        assertThat(testSuiteDTO1).isNotEqualTo(testSuiteDTO2);
        testSuiteDTO2.setId(testSuiteDTO1.getId());
        assertThat(testSuiteDTO1).isEqualTo(testSuiteDTO2);
        testSuiteDTO2.setId(2L);
        assertThat(testSuiteDTO1).isNotEqualTo(testSuiteDTO2);
        testSuiteDTO1.setId(null);
        assertThat(testSuiteDTO1).isNotEqualTo(testSuiteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(testSuiteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(testSuiteMapper.fromId(null)).isNull();
    }
}
