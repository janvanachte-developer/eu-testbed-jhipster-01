package eu.europa.ec.digit.bris.testbed.web.rest;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import eu.europa.ec.digit.bris.testbed.domain.MetaData;
import eu.europa.ec.digit.bris.testbed.repository.MetaDataRepository;
import eu.europa.ec.digit.bris.testbed.service.MetaDataService;
import eu.europa.ec.digit.bris.testbed.repository.search.MetaDataSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.MetaDataDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.MetaDataMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static eu.europa.ec.digit.bris.testbed.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetaDataResource REST controller.
 *
 * @see MetaDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestbedApp.class)
public class MetaDataResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHORS = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISHED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MetaDataRepository metaDataRepository;

    @Autowired
    private MetaDataMapper metaDataMapper;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private MetaDataSearchRepository metaDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetaDataMockMvc;

    private MetaData metaData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetaDataResource metaDataResource = new MetaDataResource(metaDataService);
        this.restMetaDataMockMvc = MockMvcBuilders.standaloneSetup(metaDataResource)
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
    public static MetaData createEntity(EntityManager em) {
        MetaData metaData = new MetaData()
            .name(DEFAULT_NAME)
            .version(DEFAULT_VERSION)
            .authors(DEFAULT_AUTHORS)
            .description(DEFAULT_DESCRIPTION)
            .published(DEFAULT_PUBLISHED)
            .lastModified(DEFAULT_LAST_MODIFIED);
        return metaData;
    }

    @Before
    public void initTest() {
        metaDataSearchRepository.deleteAll();
        metaData = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetaData() throws Exception {
        int databaseSizeBeforeCreate = metaDataRepository.findAll().size();

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);
        restMetaDataMockMvc.perform(post("/api/meta-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isCreated());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeCreate + 1);
        MetaData testMetaData = metaDataList.get(metaDataList.size() - 1);
        assertThat(testMetaData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMetaData.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testMetaData.getAuthors()).isEqualTo(DEFAULT_AUTHORS);
        assertThat(testMetaData.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMetaData.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testMetaData.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);

        // Validate the MetaData in Elasticsearch
        MetaData metaDataEs = metaDataSearchRepository.findOne(testMetaData.getId());
        assertThat(metaDataEs).isEqualToComparingFieldByField(testMetaData);
    }

    @Test
    @Transactional
    public void createMetaDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metaDataRepository.findAll().size();

        // Create the MetaData with an existing ID
        metaData.setId(1L);
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaDataMockMvc.perform(post("/api/meta-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataRepository.findAll().size();
        // set the field null
        metaData.setName(null);

        // Create the MetaData, which fails.
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        restMetaDataMockMvc.perform(post("/api/meta-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataRepository.findAll().size();
        // set the field null
        metaData.setVersion(null);

        // Create the MetaData, which fails.
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        restMetaDataMockMvc.perform(post("/api/meta-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        // Get all the metaDataList
        restMetaDataMockMvc.perform(get("/api/meta-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].authors").value(hasItem(DEFAULT_AUTHORS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(sameInstant(DEFAULT_PUBLISHED))))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED))));
    }

    @Test
    @Transactional
    public void getMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        // Get the metaData
        restMetaDataMockMvc.perform(get("/api/meta-data/{id}", metaData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metaData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.authors").value(DEFAULT_AUTHORS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.published").value(sameInstant(DEFAULT_PUBLISHED)))
            .andExpect(jsonPath("$.lastModified").value(sameInstant(DEFAULT_LAST_MODIFIED)));
    }

    @Test
    @Transactional
    public void getNonExistingMetaData() throws Exception {
        // Get the metaData
        restMetaDataMockMvc.perform(get("/api/meta-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);
        metaDataSearchRepository.save(metaData);
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();

        // Update the metaData
        MetaData updatedMetaData = metaDataRepository.findOne(metaData.getId());
        updatedMetaData
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .authors(UPDATED_AUTHORS)
            .description(UPDATED_DESCRIPTION)
            .published(UPDATED_PUBLISHED)
            .lastModified(UPDATED_LAST_MODIFIED);
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(updatedMetaData);

        restMetaDataMockMvc.perform(put("/api/meta-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isOk());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
        MetaData testMetaData = metaDataList.get(metaDataList.size() - 1);
        assertThat(testMetaData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMetaData.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMetaData.getAuthors()).isEqualTo(UPDATED_AUTHORS);
        assertThat(testMetaData.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMetaData.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testMetaData.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);

        // Validate the MetaData in Elasticsearch
        MetaData metaDataEs = metaDataSearchRepository.findOne(testMetaData.getId());
        assertThat(metaDataEs).isEqualToComparingFieldByField(testMetaData);
    }

    @Test
    @Transactional
    public void updateNonExistingMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetaDataMockMvc.perform(put("/api/meta-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isCreated());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);
        metaDataSearchRepository.save(metaData);
        int databaseSizeBeforeDelete = metaDataRepository.findAll().size();

        // Get the metaData
        restMetaDataMockMvc.perform(delete("/api/meta-data/{id}", metaData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean metaDataExistsInEs = metaDataSearchRepository.exists(metaData.getId());
        assertThat(metaDataExistsInEs).isFalse();

        // Validate the database is empty
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);
        metaDataSearchRepository.save(metaData);

        // Search the metaData
        restMetaDataMockMvc.perform(get("/api/_search/meta-data?query=id:" + metaData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].authors").value(hasItem(DEFAULT_AUTHORS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(sameInstant(DEFAULT_PUBLISHED))))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaData.class);
        MetaData metaData1 = new MetaData();
        metaData1.setId(1L);
        MetaData metaData2 = new MetaData();
        metaData2.setId(metaData1.getId());
        assertThat(metaData1).isEqualTo(metaData2);
        metaData2.setId(2L);
        assertThat(metaData1).isNotEqualTo(metaData2);
        metaData1.setId(null);
        assertThat(metaData1).isNotEqualTo(metaData2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaDataDTO.class);
        MetaDataDTO metaDataDTO1 = new MetaDataDTO();
        metaDataDTO1.setId(1L);
        MetaDataDTO metaDataDTO2 = new MetaDataDTO();
        assertThat(metaDataDTO1).isNotEqualTo(metaDataDTO2);
        metaDataDTO2.setId(metaDataDTO1.getId());
        assertThat(metaDataDTO1).isEqualTo(metaDataDTO2);
        metaDataDTO2.setId(2L);
        assertThat(metaDataDTO1).isNotEqualTo(metaDataDTO2);
        metaDataDTO1.setId(null);
        assertThat(metaDataDTO1).isNotEqualTo(metaDataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(metaDataMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(metaDataMapper.fromId(null)).isNull();
    }
}
