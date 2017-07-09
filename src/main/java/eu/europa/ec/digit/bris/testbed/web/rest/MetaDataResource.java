package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.MetaDataService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.MetaDataDTO;
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
 * REST controller for managing MetaData.
 */
@RestController
@RequestMapping("/api")
public class MetaDataResource {

    private final Logger log = LoggerFactory.getLogger(MetaDataResource.class);

    private static final String ENTITY_NAME = "metaData";

    private final MetaDataService metaDataService;

    public MetaDataResource(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    /**
     * POST  /meta-data : Create a new metaData.
     *
     * @param metaDataDTO the metaDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metaDataDTO, or with status 400 (Bad Request) if the metaData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meta-data")
    @Timed
    public ResponseEntity<MetaDataDTO> createMetaData(@Valid @RequestBody MetaDataDTO metaDataDTO) throws URISyntaxException {
        log.debug("REST request to save MetaData : {}", metaDataDTO);
        if (metaDataDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new metaData cannot already have an ID")).body(null);
        }
        MetaDataDTO result = metaDataService.save(metaDataDTO);
        return ResponseEntity.created(new URI("/api/meta-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meta-data : Updates an existing metaData.
     *
     * @param metaDataDTO the metaDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metaDataDTO,
     * or with status 400 (Bad Request) if the metaDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the metaDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meta-data")
    @Timed
    public ResponseEntity<MetaDataDTO> updateMetaData(@Valid @RequestBody MetaDataDTO metaDataDTO) throws URISyntaxException {
        log.debug("REST request to update MetaData : {}", metaDataDTO);
        if (metaDataDTO.getId() == null) {
            return createMetaData(metaDataDTO);
        }
        MetaDataDTO result = metaDataService.save(metaDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metaDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meta-data : get all the metaData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of metaData in body
     */
    @GetMapping("/meta-data")
    @Timed
    public ResponseEntity<List<MetaDataDTO>> getAllMetaData(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MetaData");
        Page<MetaDataDTO> page = metaDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meta-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meta-data/:id : get the "id" metaData.
     *
     * @param id the id of the metaDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metaDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/meta-data/{id}")
    @Timed
    public ResponseEntity<MetaDataDTO> getMetaData(@PathVariable Long id) {
        log.debug("REST request to get MetaData : {}", id);
        MetaDataDTO metaDataDTO = metaDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metaDataDTO));
    }

    /**
     * DELETE  /meta-data/:id : delete the "id" metaData.
     *
     * @param id the id of the metaDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meta-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetaData(@PathVariable Long id) {
        log.debug("REST request to delete MetaData : {}", id);
        metaDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/meta-data?query=:query : search for the metaData corresponding
     * to the query.
     *
     * @param query the query of the metaData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/meta-data")
    @Timed
    public ResponseEntity<List<MetaDataDTO>> searchMetaData(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MetaData for query {}", query);
        Page<MetaDataDTO> page = metaDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/meta-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
