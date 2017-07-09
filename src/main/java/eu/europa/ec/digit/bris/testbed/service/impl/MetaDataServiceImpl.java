package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.MetaDataService;
import eu.europa.ec.digit.bris.testbed.domain.MetaData;
import eu.europa.ec.digit.bris.testbed.repository.MetaDataRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.MetaDataSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.MetaDataDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.MetaDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MetaData.
 */
@Service
@Transactional
public class MetaDataServiceImpl implements MetaDataService{

    private final Logger log = LoggerFactory.getLogger(MetaDataServiceImpl.class);

    private final MetaDataRepository metaDataRepository;

    private final MetaDataMapper metaDataMapper;

    private final MetaDataSearchRepository metaDataSearchRepository;

    public MetaDataServiceImpl(MetaDataRepository metaDataRepository, MetaDataMapper metaDataMapper, MetaDataSearchRepository metaDataSearchRepository) {
        this.metaDataRepository = metaDataRepository;
        this.metaDataMapper = metaDataMapper;
        this.metaDataSearchRepository = metaDataSearchRepository;
    }

    /**
     * Save a metaData.
     *
     * @param metaDataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MetaDataDTO save(MetaDataDTO metaDataDTO) {
        log.debug("Request to save MetaData : {}", metaDataDTO);
        MetaData metaData = metaDataMapper.toEntity(metaDataDTO);
        metaData = metaDataRepository.save(metaData);
        MetaDataDTO result = metaDataMapper.toDto(metaData);
        metaDataSearchRepository.save(metaData);
        return result;
    }

    /**
     *  Get all the metaData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MetaDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MetaData");
        return metaDataRepository.findAll(pageable)
            .map(metaDataMapper::toDto);
    }

    /**
     *  Get one metaData by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MetaDataDTO findOne(Long id) {
        log.debug("Request to get MetaData : {}", id);
        MetaData metaData = metaDataRepository.findOne(id);
        return metaDataMapper.toDto(metaData);
    }

    /**
     *  Delete the  metaData by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MetaData : {}", id);
        metaDataRepository.delete(id);
        metaDataSearchRepository.delete(id);
    }

    /**
     * Search for the metaData corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MetaDataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MetaData for query {}", query);
        Page<MetaData> result = metaDataSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(metaDataMapper::toDto);
    }
}
