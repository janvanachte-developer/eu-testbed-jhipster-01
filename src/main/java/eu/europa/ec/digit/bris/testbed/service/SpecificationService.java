package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.SpecificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Specification.
 */
public interface SpecificationService {

    /**
     * Save a specification.
     *
     * @param specificationDTO the entity to save
     * @return the persisted entity
     */
    SpecificationDTO save(SpecificationDTO specificationDTO);

    /**
     *  Get all the specifications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SpecificationDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" specification.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SpecificationDTO findOne(Long id);

    /**
     *  Delete the "id" specification.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the specification corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SpecificationDTO> search(String query, Pageable pageable);
}
