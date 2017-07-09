package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.TestStepService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.TestStepDTO;
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
 * REST controller for managing TestStep.
 */
@RestController
@RequestMapping("/api")
public class TestStepResource {

    private final Logger log = LoggerFactory.getLogger(TestStepResource.class);

    private static final String ENTITY_NAME = "testStep";

    private final TestStepService testStepService;

    public TestStepResource(TestStepService testStepService) {
        this.testStepService = testStepService;
    }

    /**
     * POST  /test-steps : Create a new testStep.
     *
     * @param testStepDTO the testStepDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testStepDTO, or with status 400 (Bad Request) if the testStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-steps")
    @Timed
    public ResponseEntity<TestStepDTO> createTestStep(@RequestBody TestStepDTO testStepDTO) throws URISyntaxException {
        log.debug("REST request to save TestStep : {}", testStepDTO);
        if (testStepDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new testStep cannot already have an ID")).body(null);
        }
        TestStepDTO result = testStepService.save(testStepDTO);
        return ResponseEntity.created(new URI("/api/test-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-steps : Updates an existing testStep.
     *
     * @param testStepDTO the testStepDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testStepDTO,
     * or with status 400 (Bad Request) if the testStepDTO is not valid,
     * or with status 500 (Internal Server Error) if the testStepDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-steps")
    @Timed
    public ResponseEntity<TestStepDTO> updateTestStep(@RequestBody TestStepDTO testStepDTO) throws URISyntaxException {
        log.debug("REST request to update TestStep : {}", testStepDTO);
        if (testStepDTO.getId() == null) {
            return createTestStep(testStepDTO);
        }
        TestStepDTO result = testStepService.save(testStepDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testStepDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-steps : get all the testSteps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testSteps in body
     */
    @GetMapping("/test-steps")
    @Timed
    public ResponseEntity<List<TestStepDTO>> getAllTestSteps(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestSteps");
        Page<TestStepDTO> page = testStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-steps/:id : get the "id" testStep.
     *
     * @param id the id of the testStepDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testStepDTO, or with status 404 (Not Found)
     */
    @GetMapping("/test-steps/{id}")
    @Timed
    public ResponseEntity<TestStepDTO> getTestStep(@PathVariable Long id) {
        log.debug("REST request to get TestStep : {}", id);
        TestStepDTO testStepDTO = testStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testStepDTO));
    }

    /**
     * DELETE  /test-steps/:id : delete the "id" testStep.
     *
     * @param id the id of the testStepDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-steps/{id}")
    @Timed
    public ResponseEntity<Void> deleteTestStep(@PathVariable Long id) {
        log.debug("REST request to delete TestStep : {}", id);
        testStepService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/test-steps?query=:query : search for the testStep corresponding
     * to the query.
     *
     * @param query the query of the testStep search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/test-steps")
    @Timed
    public ResponseEntity<List<TestStepDTO>> searchTestSteps(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TestSteps for query {}", query);
        Page<TestStepDTO> page = testStepService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/test-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
