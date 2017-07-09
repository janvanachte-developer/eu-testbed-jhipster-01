package eu.europa.ec.digit.bris.testbed.web.rest;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import eu.europa.ec.digit.bris.testbed.domain.TestCase;
import eu.europa.ec.digit.bris.testbed.repository.TestCaseRepository;
import eu.europa.ec.digit.bris.testbed.service.TestCaseService;
import eu.europa.ec.digit.bris.testbed.repository.search.TestCaseSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestCaseDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestCaseMapper;
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
 * Test class for the TestCaseResource REST controller.
 *
 * @see TestCaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestbedApp.class)
public class TestCaseResourceIntTest {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private TestCaseSearchRepository testCaseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestCaseMockMvc;

    private TestCase testCase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestCaseResource testCaseResource = new TestCaseResource(testCaseService);
        this.restTestCaseMockMvc = MockMvcBuilders.standaloneSetup(testCaseResource)
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
    public static TestCase createEntity(EntityManager em) {
        TestCase testCase = new TestCase();
        return testCase;
    }

    @Before
    public void initTest() {
        testCaseSearchRepository.deleteAll();
        testCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestCase() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);
        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate + 1);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);

        // Validate the TestCase in Elasticsearch
        TestCase testCaseEs = testCaseSearchRepository.findOne(testTestCase.getId());
        assertThat(testCaseEs).isEqualToComparingFieldByField(testTestCase);
    }

    @Test
    @Transactional
    public void createTestCaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // Create the TestCase with an existing ID
        testCase.setId(1L);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestCases() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList
        restTestCaseMockMvc.perform(get("/api/test-cases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get the testCase
        restTestCaseMockMvc.perform(get("/api/test-cases/{id}", testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testCase.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTestCase() throws Exception {
        // Get the testCase
        restTestCaseMockMvc.perform(get("/api/test-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);
        testCaseSearchRepository.save(testCase);
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findOne(testCase.getId());
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(updatedTestCase);

        restTestCaseMockMvc.perform(put("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);

        // Validate the TestCase in Elasticsearch
        TestCase testCaseEs = testCaseSearchRepository.findOne(testTestCase.getId());
        assertThat(testCaseEs).isEqualToComparingFieldByField(testTestCase);
    }

    @Test
    @Transactional
    public void updateNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestCaseMockMvc.perform(put("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);
        testCaseSearchRepository.save(testCase);
        int databaseSizeBeforeDelete = testCaseRepository.findAll().size();

        // Get the testCase
        restTestCaseMockMvc.perform(delete("/api/test-cases/{id}", testCase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean testCaseExistsInEs = testCaseSearchRepository.exists(testCase.getId());
        assertThat(testCaseExistsInEs).isFalse();

        // Validate the database is empty
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);
        testCaseSearchRepository.save(testCase);

        // Search the testCase
        restTestCaseMockMvc.perform(get("/api/_search/test-cases?query=id:" + testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCase.class);
        TestCase testCase1 = new TestCase();
        testCase1.setId(1L);
        TestCase testCase2 = new TestCase();
        testCase2.setId(testCase1.getId());
        assertThat(testCase1).isEqualTo(testCase2);
        testCase2.setId(2L);
        assertThat(testCase1).isNotEqualTo(testCase2);
        testCase1.setId(null);
        assertThat(testCase1).isNotEqualTo(testCase2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseDTO.class);
        TestCaseDTO testCaseDTO1 = new TestCaseDTO();
        testCaseDTO1.setId(1L);
        TestCaseDTO testCaseDTO2 = new TestCaseDTO();
        assertThat(testCaseDTO1).isNotEqualTo(testCaseDTO2);
        testCaseDTO2.setId(testCaseDTO1.getId());
        assertThat(testCaseDTO1).isEqualTo(testCaseDTO2);
        testCaseDTO2.setId(2L);
        assertThat(testCaseDTO1).isNotEqualTo(testCaseDTO2);
        testCaseDTO1.setId(null);
        assertThat(testCaseDTO1).isNotEqualTo(testCaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(testCaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(testCaseMapper.fromId(null)).isNull();
    }
}
