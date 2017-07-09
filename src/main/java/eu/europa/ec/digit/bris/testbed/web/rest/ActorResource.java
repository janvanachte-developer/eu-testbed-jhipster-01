package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.ActorService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.ActorDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Actor.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "actor";

    private final ActorService actorService;

    public ActorResource(ActorService actorService) {
        this.actorService = actorService;
    }

    /**
     * POST  /actors : Create a new actor.
     *
     * @param actorDTO the actorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actorDTO, or with status 400 (Bad Request) if the actor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/actors")
    @Timed
    public ResponseEntity<ActorDTO> createActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actorDTO);
        if (actorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new actor cannot already have an ID")).body(null);
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity.created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actors : Updates an existing actor.
     *
     * @param actorDTO the actorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actorDTO,
     * or with status 400 (Bad Request) if the actorDTO is not valid,
     * or with status 500 (Internal Server Error) if the actorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/actors")
    @Timed
    public ResponseEntity<ActorDTO> updateActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to update Actor : {}", actorDTO);
        if (actorDTO.getId() == null) {
            return createActor(actorDTO);
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actors : get all the actors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of actors in body
     */
    @GetMapping("/actors")
    @Timed
    public ResponseEntity<List<ActorDTO>> getAllActors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Actors");
        Page<ActorDTO> page = actorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/actors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /actors/:id : get the "id" actor.
     *
     * @param id the id of the actorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/actors/{id}")
    @Timed
    public ResponseEntity<ActorDTO> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        ActorDTO actorDTO = actorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(actorDTO));
    }

    /**
     * DELETE  /actors/:id : delete the "id" actor.
     *
     * @param id the id of the actorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/actors/{id}")
    @Timed
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/actors?query=:query : search for the actor corresponding
     * to the query.
     *
     * @param query the query of the actor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/actors")
    @Timed
    public ResponseEntity<List<ActorDTO>> searchActors(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Actors for query {}", query);
        Page<ActorDTO> page = actorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/actors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
