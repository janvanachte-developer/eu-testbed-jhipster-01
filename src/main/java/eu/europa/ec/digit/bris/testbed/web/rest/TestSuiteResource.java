package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.TestSuiteService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.TestSuiteDTO;
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
 * REST controller for managing TestSuite.
 */
@RestController
@RequestMapping("/api")
public class TestSuiteResource {

    private final Logger log = LoggerFactory.getLogger(TestSuiteResource.class);

    private static final String ENTITY_NAME = "testSuite";

    private final TestSuiteService testSuiteService;

    public TestSuiteResource(TestSuiteService testSuiteService) {
        this.testSuiteService = testSuiteService;
    }

    /**
     * POST  /test-suites : Create a new testSuite.
     *
     * @param testSuiteDTO the testSuiteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testSuiteDTO, or with status 400 (Bad Request) if the testSuite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-suites")
    @Timed
    public ResponseEntity<TestSuiteDTO> createTestSuite(@RequestBody TestSuiteDTO testSuiteDTO) throws URISyntaxException {
        log.debug("REST request to save TestSuite : {}", testSuiteDTO);
        if (testSuiteDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new testSuite cannot already have an ID")).body(null);
        }
        TestSuiteDTO result = testSuiteService.save(testSuiteDTO);
        return ResponseEntity.created(new URI("/api/test-suites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-suites : Updates an existing testSuite.
     *
     * @param testSuiteDTO the testSuiteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testSuiteDTO,
     * or with status 400 (Bad Request) if the testSuiteDTO is not valid,
     * or with status 500 (Internal Server Error) if the testSuiteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-suites")
    @Timed
    public ResponseEntity<TestSuiteDTO> updateTestSuite(@RequestBody TestSuiteDTO testSuiteDTO) throws URISyntaxException {
        log.debug("REST request to update TestSuite : {}", testSuiteDTO);
        if (testSuiteDTO.getId() == null) {
            return createTestSuite(testSuiteDTO);
        }
        TestSuiteDTO result = testSuiteService.save(testSuiteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testSuiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-suites : get all the testSuites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testSuites in body
     */
    @GetMapping("/test-suites")
    @Timed
    public ResponseEntity<List<TestSuiteDTO>> getAllTestSuites(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestSuites");
        Page<TestSuiteDTO> page = testSuiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-suites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-suites/:id : get the "id" testSuite.
     *
     * @param id the id of the testSuiteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testSuiteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/test-suites/{id}")
    @Timed
    public ResponseEntity<TestSuiteDTO> getTestSuite(@PathVariable Long id) {
        log.debug("REST request to get TestSuite : {}", id);
        TestSuiteDTO testSuiteDTO = testSuiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testSuiteDTO));
    }

    /**
     * DELETE  /test-suites/:id : delete the "id" testSuite.
     *
     * @param id the id of the testSuiteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-suites/{id}")
    @Timed
    public ResponseEntity<Void> deleteTestSuite(@PathVariable Long id) {
        log.debug("REST request to delete TestSuite : {}", id);
        testSuiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/test-suites?query=:query : search for the testSuite corresponding
     * to the query.
     *
     * @param query the query of the testSuite search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/test-suites")
    @Timed
    public ResponseEntity<List<TestSuiteDTO>> searchTestSuites(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TestSuites for query {}", query);
        Page<TestSuiteDTO> page = testSuiteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/test-suites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
