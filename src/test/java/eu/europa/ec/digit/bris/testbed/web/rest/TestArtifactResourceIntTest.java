package eu.europa.ec.digit.bris.testbed.web.rest;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import eu.europa.ec.digit.bris.testbed.domain.TestArtifact;
import eu.europa.ec.digit.bris.testbed.repository.TestArtifactRepository;
import eu.europa.ec.digit.bris.testbed.service.TestArtifactService;
import eu.europa.ec.digit.bris.testbed.repository.search.TestArtifactSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.TestArtifactDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.TestArtifactMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TestArtifactResource REST controller.
 *
 * @see TestArtifactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestbedApp.class)
public class TestArtifactResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENCOFING = "AAAAAAAAAA";
    private static final String UPDATED_ENCOFING = "BBBBBBBBBB";

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private TestArtifactRepository testArtifactRepository;

    @Autowired
    private TestArtifactMapper testArtifactMapper;

    @Autowired
    private TestArtifactService testArtifactService;

    @Autowired
    private TestArtifactSearchRepository testArtifactSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestArtifactMockMvc;

    private TestArtifact testArtifact;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestArtifactResource testArtifactResource = new TestArtifactResource(testArtifactService);
        this.restTestArtifactMockMvc = MockMvcBuilders.standaloneSetup(testArtifactResource)
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
    public static TestArtifact createEntity(EntityManager em) {
        TestArtifact testArtifact = new TestArtifact()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .encofing(DEFAULT_ENCOFING)
            .uri(DEFAULT_URI)
            .content(DEFAULT_CONTENT);
        return testArtifact;
    }

    @Before
    public void initTest() {
        testArtifactSearchRepository.deleteAll();
        testArtifact = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestArtifact() throws Exception {
        int databaseSizeBeforeCreate = testArtifactRepository.findAll().size();

        // Create the TestArtifact
        TestArtifactDTO testArtifactDTO = testArtifactMapper.toDto(testArtifact);
        restTestArtifactMockMvc.perform(post("/api/test-artifacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testArtifactDTO)))
            .andExpect(status().isCreated());

        // Validate the TestArtifact in the database
        List<TestArtifact> testArtifactList = testArtifactRepository.findAll();
        assertThat(testArtifactList).hasSize(databaseSizeBeforeCreate + 1);
        TestArtifact testTestArtifact = testArtifactList.get(testArtifactList.size() - 1);
        assertThat(testTestArtifact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestArtifact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTestArtifact.getEncofing()).isEqualTo(DEFAULT_ENCOFING);
        assertThat(testTestArtifact.getUri()).isEqualTo(DEFAULT_URI);
        assertThat(testTestArtifact.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the TestArtifact in Elasticsearch
        TestArtifact testArtifactEs = testArtifactSearchRepository.findOne(testTestArtifact.getId());
        assertThat(testArtifactEs).isEqualToComparingFieldByField(testTestArtifact);
    }

    @Test
    @Transactional
    public void createTestArtifactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testArtifactRepository.findAll().size();

        // Create the TestArtifact with an existing ID
        testArtifact.setId(1L);
        TestArtifactDTO testArtifactDTO = testArtifactMapper.toDto(testArtifact);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestArtifactMockMvc.perform(post("/api/test-artifacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testArtifactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TestArtifact> testArtifactList = testArtifactRepository.findAll();
        assertThat(testArtifactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestArtifacts() throws Exception {
        // Initialize the database
        testArtifactRepository.saveAndFlush(testArtifact);

        // Get all the testArtifactList
        restTestArtifactMockMvc.perform(get("/api/test-artifacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testArtifact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].encofing").value(hasItem(DEFAULT_ENCOFING.toString())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getTestArtifact() throws Exception {
        // Initialize the database
        testArtifactRepository.saveAndFlush(testArtifact);

        // Get the testArtifact
        restTestArtifactMockMvc.perform(get("/api/test-artifacts/{id}", testArtifact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testArtifact.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.encofing").value(DEFAULT_ENCOFING.toString()))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestArtifact() throws Exception {
        // Get the testArtifact
        restTestArtifactMockMvc.perform(get("/api/test-artifacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestArtifact() throws Exception {
        // Initialize the database
        testArtifactRepository.saveAndFlush(testArtifact);
        testArtifactSearchRepository.save(testArtifact);
        int databaseSizeBeforeUpdate = testArtifactRepository.findAll().size();

        // Update the testArtifact
        TestArtifact updatedTestArtifact = testArtifactRepository.findOne(testArtifact.getId());
        updatedTestArtifact
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .encofing(UPDATED_ENCOFING)
            .uri(UPDATED_URI)
            .content(UPDATED_CONTENT);
        TestArtifactDTO testArtifactDTO = testArtifactMapper.toDto(updatedTestArtifact);

        restTestArtifactMockMvc.perform(put("/api/test-artifacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testArtifactDTO)))
            .andExpect(status().isOk());

        // Validate the TestArtifact in the database
        List<TestArtifact> testArtifactList = testArtifactRepository.findAll();
        assertThat(testArtifactList).hasSize(databaseSizeBeforeUpdate);
        TestArtifact testTestArtifact = testArtifactList.get(testArtifactList.size() - 1);
        assertThat(testTestArtifact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestArtifact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTestArtifact.getEncofing()).isEqualTo(UPDATED_ENCOFING);
        assertThat(testTestArtifact.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testTestArtifact.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the TestArtifact in Elasticsearch
        TestArtifact testArtifactEs = testArtifactSearchRepository.findOne(testTestArtifact.getId());
        assertThat(testArtifactEs).isEqualToComparingFieldByField(testTestArtifact);
    }

    @Test
    @Transactional
    public void updateNonExistingTestArtifact() throws Exception {
        int databaseSizeBeforeUpdate = testArtifactRepository.findAll().size();

        // Create the TestArtifact
        TestArtifactDTO testArtifactDTO = testArtifactMapper.toDto(testArtifact);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestArtifactMockMvc.perform(put("/api/test-artifacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testArtifactDTO)))
            .andExpect(status().isCreated());

        // Validate the TestArtifact in the database
        List<TestArtifact> testArtifactList = testArtifactRepository.findAll();
        assertThat(testArtifactList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestArtifact() throws Exception {
        // Initialize the database
        testArtifactRepository.saveAndFlush(testArtifact);
        testArtifactSearchRepository.save(testArtifact);
        int databaseSizeBeforeDelete = testArtifactRepository.findAll().size();

        // Get the testArtifact
        restTestArtifactMockMvc.perform(delete("/api/test-artifacts/{id}", testArtifact.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean testArtifactExistsInEs = testArtifactSearchRepository.exists(testArtifact.getId());
        assertThat(testArtifactExistsInEs).isFalse();

        // Validate the database is empty
        List<TestArtifact> testArtifactList = testArtifactRepository.findAll();
        assertThat(testArtifactList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTestArtifact() throws Exception {
        // Initialize the database
        testArtifactRepository.saveAndFlush(testArtifact);
        testArtifactSearchRepository.save(testArtifact);

        // Search the testArtifact
        restTestArtifactMockMvc.perform(get("/api/_search/test-artifacts?query=id:" + testArtifact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testArtifact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].encofing").value(hasItem(DEFAULT_ENCOFING.toString())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestArtifact.class);
        TestArtifact testArtifact1 = new TestArtifact();
        testArtifact1.setId(1L);
        TestArtifact testArtifact2 = new TestArtifact();
        testArtifact2.setId(testArtifact1.getId());
        assertThat(testArtifact1).isEqualTo(testArtifact2);
        testArtifact2.setId(2L);
        assertThat(testArtifact1).isNotEqualTo(testArtifact2);
        testArtifact1.setId(null);
        assertThat(testArtifact1).isNotEqualTo(testArtifact2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestArtifactDTO.class);
        TestArtifactDTO testArtifactDTO1 = new TestArtifactDTO();
        testArtifactDTO1.setId(1L);
        TestArtifactDTO testArtifactDTO2 = new TestArtifactDTO();
        assertThat(testArtifactDTO1).isNotEqualTo(testArtifactDTO2);
        testArtifactDTO2.setId(testArtifactDTO1.getId());
        assertThat(testArtifactDTO1).isEqualTo(testArtifactDTO2);
        testArtifactDTO2.setId(2L);
        assertThat(testArtifactDTO1).isNotEqualTo(testArtifactDTO2);
        testArtifactDTO1.setId(null);
        assertThat(testArtifactDTO1).isNotEqualTo(testArtifactDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(testArtifactMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(testArtifactMapper.fromId(null)).isNull();
    }
}
