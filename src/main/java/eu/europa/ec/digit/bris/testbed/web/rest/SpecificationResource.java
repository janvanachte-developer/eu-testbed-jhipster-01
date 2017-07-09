package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.SpecificationService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.SpecificationDTO;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Specification.
 */
@RestController
@RequestMapping("/api")
public class SpecificationResource {

    private final Logger log = LoggerFactory.getLogger(SpecificationResource.class);

    private static final String ENTITY_NAME = "specification";

    private final SpecificationService specificationService;

    public SpecificationResource(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    /**
     * POST  /specifications : Create a new specification.
     *
     * @param specificationDTO the specificationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new specificationDTO, or with status 400 (Bad Request) if the specification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/specifications")
    @Timed
    public ResponseEntity<SpecificationDTO> createSpecification(@RequestBody SpecificationDTO specificationDTO) throws URISyntaxException {
        log.debug("REST request to save Specification : {}", specificationDTO);
        if (specificationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new specification cannot already have an ID")).body(null);
        }
        SpecificationDTO result = specificationService.save(specificationDTO);
        return ResponseEntity.created(new URI("/api/specifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /specifications : Updates an existing specification.
     *
     * @param specificationDTO the specificationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated specificationDTO,
     * or with status 400 (Bad Request) if the specificationDTO is not valid,
     * or with status 500 (Internal Server Error) if the specificationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/specifications")
    @Timed
    public ResponseEntity<SpecificationDTO> updateSpecification(@RequestBody SpecificationDTO specificationDTO) throws URISyntaxException {
        log.debug("REST request to update Specification : {}", specificationDTO);
        if (specificationDTO.getId() == null) {
            return createSpecification(specificationDTO);
        }
        SpecificationDTO result = specificationService.save(specificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, specificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /specifications : get all the specifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of specifications in body
     */
    @GetMapping("/specifications")
    @Timed
    public ResponseEntity<List<SpecificationDTO>> getAllSpecifications(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Specifications");
        Page<SpecificationDTO> page = specificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/specifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /specifications/:id : get the "id" specification.
     *
     * @param id the id of the specificationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the specificationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/specifications/{id}")
    @Timed
    public ResponseEntity<SpecificationDTO> getSpecification(@PathVariable Long id) {
        log.debug("REST request to get Specification : {}", id);
        SpecificationDTO specificationDTO = specificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(specificationDTO));
    }

    /**
     * DELETE  /specifications/:id : delete the "id" specification.
     *
     * @param id the id of the specificationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/specifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long id) {
        log.debug("REST request to delete Specification : {}", id);
        specificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/specifications?query=:query : search for the specification corresponding
     * to the query.
     *
     * @param query the query of the specification search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/specifications")
    @Timed
    public ResponseEntity<List<SpecificationDTO>> searchSpecifications(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Specifications for query {}", query);
        Page<SpecificationDTO> page = specificationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/specifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
