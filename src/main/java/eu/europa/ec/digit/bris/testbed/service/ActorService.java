package eu.europa.ec.digit.bris.testbed.service;

import eu.europa.ec.digit.bris.testbed.service.dto.ActorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Actor.
 */
public interface ActorService {

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save
     * @return the persisted entity
     */
    ActorDTO save(ActorDTO actorDTO);

    /**
     *  Get all the actors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ActorDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" actor.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ActorDTO findOne(Long id);

    /**
     *  Delete the "id" actor.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the actor corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ActorDTO> search(String query, Pageable pageable);
}
