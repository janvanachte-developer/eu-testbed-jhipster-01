package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.DomainService;
import eu.europa.ec.digit.bris.testbed.domain.Domain;
import eu.europa.ec.digit.bris.testbed.repository.DomainRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.DomainSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.DomainDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.DomainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Domain.
 */
@Service
@Transactional
public class DomainServiceImpl implements DomainService{

    private final Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);

    private final DomainRepository domainRepository;

    private final DomainMapper domainMapper;

    private final DomainSearchRepository domainSearchRepository;

    public DomainServiceImpl(DomainRepository domainRepository, DomainMapper domainMapper, DomainSearchRepository domainSearchRepository) {
        this.domainRepository = domainRepository;
        this.domainMapper = domainMapper;
        this.domainSearchRepository = domainSearchRepository;
    }

    /**
     * Save a domain.
     *
     * @param domainDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DomainDTO save(DomainDTO domainDTO) {
        log.debug("Request to save Domain : {}", domainDTO);
        Domain domain = domainMapper.toEntity(domainDTO);
        domain = domainRepository.save(domain);
        DomainDTO result = domainMapper.toDto(domain);
        domainSearchRepository.save(domain);
        return result;
    }

    /**
     *  Get all the domains.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DomainDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Domains");
        return domainRepository.findAll(pageable)
            .map(domainMapper::toDto);
    }

    /**
     *  Get one domain by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DomainDTO findOne(Long id) {
        log.debug("Request to get Domain : {}", id);
        Domain domain = domainRepository.findOne(id);
        return domainMapper.toDto(domain);
    }

    /**
     *  Delete the  domain by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Domain : {}", id);
        domainRepository.delete(id);
        domainSearchRepository.delete(id);
    }

    /**
     * Search for the domain corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DomainDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Domains for query {}", query);
        Page<Domain> result = domainSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(domainMapper::toDto);
    }
}
