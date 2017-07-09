package eu.europa.ec.digit.bris.testbed.service.impl;

import eu.europa.ec.digit.bris.testbed.service.ActorService;
import eu.europa.ec.digit.bris.testbed.domain.Actor;
import eu.europa.ec.digit.bris.testbed.repository.ActorRepository;
import eu.europa.ec.digit.bris.testbed.repository.search.ActorSearchRepository;
import eu.europa.ec.digit.bris.testbed.service.dto.ActorDTO;
import eu.europa.ec.digit.bris.testbed.service.mapper.ActorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Actor.
 */
@Service
@Transactional
public class ActorServiceImpl implements ActorService{

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    private final ActorSearchRepository actorSearchRepository;

    public ActorServiceImpl(ActorRepository actorRepository, ActorMapper actorMapper, ActorSearchRepository actorSearchRepository) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.actorSearchRepository = actorSearchRepository;
    }

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ActorDTO save(ActorDTO actorDTO) {
        log.debug("Request to save Actor : {}", actorDTO);
        Actor actor = actorMapper.toEntity(actorDTO);
        actor = actorRepository.save(actor);
        ActorDTO result = actorMapper.toDto(actor);
        actorSearchRepository.save(actor);
        return result;
    }

    /**
     *  Get all the actors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Actors");
        return actorRepository.findAll(pageable)
            .map(actorMapper::toDto);
    }

    /**
     *  Get one actor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ActorDTO findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        Actor actor = actorRepository.findOne(id);
        return actorMapper.toDto(actor);
    }

    /**
     *  Delete the  actor by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        actorRepository.delete(id);
        actorSearchRepository.delete(id);
    }

    /**
     * Search for the actor corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Actors for query {}", query);
        Page<Actor> result = actorSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(actorMapper::toDto);
    }
}
