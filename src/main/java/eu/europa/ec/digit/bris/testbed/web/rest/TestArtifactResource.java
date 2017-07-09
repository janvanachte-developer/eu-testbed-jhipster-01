package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.TestArtifactService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.TestArtifactDTO;
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
 * REST controller for managing TestArtifact.
 */
@RestController
@RequestMapping("/api")
public class TestArtifactResource {

    private final Logger log = LoggerFactory.getLogger(TestArtifactResource.class);

    private static final String ENTITY_NAME = "testArtifact";

    private final TestArtifactService testArtifactService;

    public TestArtifactResource(TestArtifactService testArtifactService) {
        this.testArtifactService = testArtifactService;
    }

    /**
     * POST  /test-artifacts : Create a new testArtifact.
     *
     * @param testArtifactDTO the testArtifactDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testArtifactDTO, or with status 400 (Bad Request) if the testArtifact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-artifacts")
    @Timed
    public ResponseEntity<TestArtifactDTO> createTestArtifact(@RequestBody TestArtifactDTO testArtifactDTO) throws URISyntaxException {
        log.debug("REST request to save TestArtifact : {}", testArtifactDTO);
        if (testArtifactDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new testArtifact cannot already have an ID")).body(null);
        }
        TestArtifactDTO result = testArtifactService.save(testArtifactDTO);
        return ResponseEntity.created(new URI("/api/test-artifacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-artifacts : Updates an existing testArtifact.
     *
     * @param testArtifactDTO the testArtifactDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testArtifactDTO,
     * or with status 400 (Bad Request) if the testArtifactDTO is not valid,
     * or with status 500 (Internal Server Error) if the testArtifactDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-artifacts")
    @Timed
    public ResponseEntity<TestArtifactDTO> updateTestArtifact(@RequestBody TestArtifactDTO testArtifactDTO) throws URISyntaxException {
        log.debug("REST request to update TestArtifact : {}", testArtifactDTO);
        if (testArtifactDTO.getId() == null) {
            return createTestArtifact(testArtifactDTO);
        }
        TestArtifactDTO result = testArtifactService.save(testArtifactDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testArtifactDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-artifacts : get all the testArtifacts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testArtifacts in body
     */
    @GetMapping("/test-artifacts")
    @Timed
    public ResponseEntity<List<TestArtifactDTO>> getAllTestArtifacts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestArtifacts");
        Page<TestArtifactDTO> page = testArtifactService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-artifacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-artifacts/:id : get the "id" testArtifact.
     *
     * @param id the id of the testArtifactDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testArtifactDTO, or with status 404 (Not Found)
     */
    @GetMapping("/test-artifacts/{id}")
    @Timed
    public ResponseEntity<TestArtifactDTO> getTestArtifact(@PathVariable Long id) {
        log.debug("REST request to get TestArtifact : {}", id);
        TestArtifactDTO testArtifactDTO = testArtifactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testArtifactDTO));
    }

    /**
     * DELETE  /test-artifacts/:id : delete the "id" testArtifact.
     *
     * @param id the id of the testArtifactDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-artifacts/{id}")
    @Timed
    public ResponseEntity<Void> deleteTestArtifact(@PathVariable Long id) {
        log.debug("REST request to delete TestArtifact : {}", id);
        testArtifactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/test-artifacts?query=:query : search for the testArtifact corresponding
     * to the query.
     *
     * @param query the query of the testArtifact search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/test-artifacts")
    @Timed
    public ResponseEntity<List<TestArtifactDTO>> searchTestArtifacts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TestArtifacts for query {}", query);
        Page<TestArtifactDTO> page = testArtifactService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/test-artifacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
