package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.SpecificationService;
import eu.europa.ec.digit.bris.testbed.domain.Specification;
import eu.europa.ec.digit.bris.testbed.repository.SpecificationRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.SpecificationSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.SpecificationDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.SpecificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Specification.
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService{

    private final Logger log = LoggerFactory.getLogger(SpecificationServiceImpl.class);

    private final SpecificationRepository specificationRepository;

    private final SpecificationMapper specificationMapper;

    private final SpecificationSearchRepository specificationSearchRepository;

    public SpecificationServiceImpl(SpecificationRepository specificationRepository, SpecificationMapper specificationMapper, SpecificationSearchRepository specificationSearchRepository) {
        this.specificationRepository = specificationRepository;
        this.specificationMapper = specificationMapper;
        this.specificationSearchRepository = specificationSearchRepository;
    }

    /**
     * Save a specification.
     *
     * @param specificationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SpecificationDTO save(SpecificationDTO specificationDTO) {
        log.debug("Request to save Specification : {}", specificationDTO);
        Specification specification = specificationMapper.toEntity(specificationDTO);
        specification = specificationRepository.save(specification);
        SpecificationDTO result = specificationMapper.toDto(specification);
        specificationSearchRepository.save(specification);
        return result;
    }

    /**
     *  Get all the specifications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SpecificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Specifications");
        return specificationRepository.findAll(pageable)
            .map(specificationMapper::toDto);
    }

    /**
     *  Get one specification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SpecificationDTO findOne(Long id) {
        log.debug("Request to get Specification : {}", id);
        Specification specification = specificationRepository.findOne(id);
        return specificationMapper.toDto(specification);
    }

    /**
     *  Delete the  specification by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Specification : {}", id);
        specificationRepository.delete(id);
        specificationSearchRepository.delete(id);
    }

    /**
     * Search for the specification corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SpecificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Specifications for query {}", query);
        Page<Specification> result = specificationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(specificationMapper::toDto);
    }
}
