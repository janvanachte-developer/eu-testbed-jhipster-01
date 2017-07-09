package eu.europa.ec.digit.bris.testbed.web.rest;

import eu.europa.ec.digit.bris.testbed.TestbedApp;

import eu.europa.ec.digit.bris.testbed.domain.Specification;
import eu.europa.ec.digit.bris.testbed.repository.SpecificationRepository;
import eu.europa.ec.digit.bris.testbed.service.SpecificationService;
import eu.europa.ec.digit.bris.testbed.repository.search.SpecificationSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.SpecificationDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.SpecificationMapper;
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
 * Test class for the SpecificationResource REST controller.
 *
 * @see SpecificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestbedApp.class)
public class SpecificationResourceIntTest {

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private SpecificationSearchRepository specificationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSpecificationMockMvc;

    private Specification specification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpecificationResource specificationResource = new SpecificationResource(specificationService);
        this.restSpecificationMockMvc = MockMvcBuilders.standaloneSetup(specificationResource)
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
    public static Specification createEntity(EntityManager em) {
        Specification specification = new Specification();
        return specification;
    }

    @Before
    public void initTest() {
        specificationSearchRepository.deleteAll();
        specification = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecification() throws Exception {
        int databaseSizeBeforeCreate = specificationRepository.findAll().size();

        // Create the Specification
        SpecificationDTO specificationDTO = specificationMapper.toDto(specification);
        restSpecificationMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Specification in the database
        List<Specification> specificationList = specificationRepository.findAll();
        assertThat(specificationList).hasSize(databaseSizeBeforeCreate + 1);
        Specification testSpecification = specificationList.get(specificationList.size() - 1);

        // Validate the Specification in Elasticsearch
        Specification specificationEs = specificationSearchRepository.findOne(testSpecification.getId());
        assertThat(specificationEs).isEqualToComparingFieldByField(testSpecification);
    }

    @Test
    @Transactional
    public void createSpecificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specificationRepository.findAll().size();

        // Create the Specification with an existing ID
        specification.setId(1L);
        SpecificationDTO specificationDTO = specificationMapper.toDto(specification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecificationMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Specification> specificationList = specificationRepository.findAll();
        assertThat(specificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSpecifications() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);

        // Get all the specificationList
        restSpecificationMockMvc.perform(get("/api/specifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specification.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);

        // Get the specification
        restSpecificationMockMvc.perform(get("/api/specifications/{id}", specification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(specification.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSpecification() throws Exception {
        // Get the specification
        restSpecificationMockMvc.perform(get("/api/specifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);
        specificationSearchRepository.save(specification);
        int databaseSizeBeforeUpdate = specificationRepository.findAll().size();

        // Update the specification
        Specification updatedSpecification = specificationRepository.findOne(specification.getId());
        SpecificationDTO specificationDTO = specificationMapper.toDto(updatedSpecification);

        restSpecificationMockMvc.perform(put("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationDTO)))
            .andExpect(status().isOk());

        // Validate the Specification in the database
        List<Specification> specificationList = specificationRepository.findAll();
        assertThat(specificationList).hasSize(databaseSizeBeforeUpdate);
        Specification testSpecification = specificationList.get(specificationList.size() - 1);

        // Validate the Specification in Elasticsearch
        Specification specificationEs = specificationSearchRepository.findOne(testSpecification.getId());
        assertThat(specificationEs).isEqualToComparingFieldByField(testSpecification);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecification() throws Exception {
        int databaseSizeBeforeUpdate = specificationRepository.findAll().size();

        // Create the Specification
        SpecificationDTO specificationDTO = specificationMapper.toDto(specification);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpecificationMockMvc.perform(put("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Specification in the database
        List<Specification> specificationList = specificationRepository.findAll();
        assertThat(specificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);
        specificationSearchRepository.save(specification);
        int databaseSizeBeforeDelete = specificationRepository.findAll().size();

        // Get the specification
        restSpecificationMockMvc.perform(delete("/api/specifications/{id}", specification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean specificationExistsInEs = specificationSearchRepository.exists(specification.getId());
        assertThat(specificationExistsInEs).isFalse();

        // Validate the database is empty
        List<Specification> specificationList = specificationRepository.findAll();
        assertThat(specificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);
        specificationSearchRepository.save(specification);

        // Search the specification
        restSpecificationMockMvc.perform(get("/api/_search/specifications?query=id:" + specification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specification.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specification.class);
        Specification specification1 = new Specification();
        specification1.setId(1L);
        Specification specification2 = new Specification();
        specification2.setId(specification1.getId());
        assertThat(specification1).isEqualTo(specification2);
        specification2.setId(2L);
        assertThat(specification1).isNotEqualTo(specification2);
        specification1.setId(null);
        assertThat(specification1).isNotEqualTo(specification2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecificationDTO.class);
        SpecificationDTO specificationDTO1 = new SpecificationDTO();
        specificationDTO1.setId(1L);
        SpecificationDTO specificationDTO2 = new SpecificationDTO();
        assertThat(specificationDTO1).isNotEqualTo(specificationDTO2);
        specificationDTO2.setId(specificationDTO1.getId());
        assertThat(specificationDTO1).isEqualTo(specificationDTO2);
        specificationDTO2.setId(2L);
        assertThat(specificationDTO1).isNotEqualTo(specificationDTO2);
        specificationDTO1.setId(null);
        assertThat(specificationDTO1).isNotEqualTo(specificationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(specificationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(specificationMapper.fromId(null)).isNull();
    }
}
