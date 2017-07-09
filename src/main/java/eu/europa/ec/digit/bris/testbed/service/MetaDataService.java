package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.MetaDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MetaData.
 */
public interface MetaDataService {

    /**
     * Save a metaData.
     *
     * @param metaDataDTO the entity to save
     * @return the persisted entity
     */
    MetaDataDTO save(MetaDataDTO metaDataDTO);

    /**
     *  Get all the metaData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MetaDataDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" metaData.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MetaDataDTO findOne(Long id);

    /**
     *  Delete the "id" metaData.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the metaData corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MetaDataDTO> search(String query, Pageable pageable);
}
