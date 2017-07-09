package eu.europa.ec.digit.bris.testbed.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.europa.ec.digit.bris.testbed.service.TestCaseService;
import eu.europa.ec.digit.bris.testbed.web.rest.util.HeaderUtil;
import eu.europa.ec.digit.bris.testbed.web.rest.util.PaginationUtil;
import eu.europa.ec.digit.bris.testbed.service.dto.TestCaseDTO;
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
 * REST controller for managing TestCase.
 */
@RestController
@RequestMapping("/api")
public class TestCaseResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseResource.class);

    private static final String ENTITY_NAME = "testCase";

    private final TestCaseService testCaseService;

    public TestCaseResource(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    /**
     * POST  /test-cases : Create a new testCase.
     *
     * @param testCaseDTO the testCaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testCaseDTO, or with status 400 (Bad Request) if the testCase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-cases")
    @Timed
    public ResponseEntity<TestCaseDTO> createTestCase(@RequestBody TestCaseDTO testCaseDTO) throws URISyntaxException {
        log.debug("REST request to save TestCase : {}", testCaseDTO);
        if (testCaseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new testCase cannot already have an ID")).body(null);
        }
        TestCaseDTO result = testCaseService.save(testCaseDTO);
        return ResponseEntity.created(new URI("/api/test-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-cases : Updates an existing testCase.
     *
     * @param testCaseDTO the testCaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testCaseDTO,
     * or with status 400 (Bad Request) if the testCaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the testCaseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-cases")
    @Timed
    public ResponseEntity<TestCaseDTO> updateTestCase(@RequestBody TestCaseDTO testCaseDTO) throws URISyntaxException {
        log.debug("REST request to update TestCase : {}", testCaseDTO);
        if (testCaseDTO.getId() == null) {
            return createTestCase(testCaseDTO);
        }
        TestCaseDTO result = testCaseService.save(testCaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testCaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-cases : get all the testCases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testCases in body
     */
    @GetMapping("/test-cases")
    @Timed
    public ResponseEntity<List<TestCaseDTO>> getAllTestCases(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestCases");
        Page<TestCaseDTO> page = testCaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-cases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-cases/:id : get the "id" testCase.
     *
     * @param id the id of the testCaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testCaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/test-cases/{id}")
    @Timed
    public ResponseEntity<TestCaseDTO> getTestCase(@PathVariable Long id) {
        log.debug("REST request to get TestCase : {}", id);
        TestCaseDTO testCaseDTO = testCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testCaseDTO));
    }

    /**
     * DELETE  /test-cases/:id : delete the "id" testCase.
     *
     * @param id the id of the testCaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-cases/{id}")
    @Timed
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        log.debug("REST request to delete TestCase : {}", id);
        testCaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/test-cases?query=:query : search for the testCase corresponding
     * to the query.
     *
     * @param query the query of the testCase search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/test-cases")
    @Timed
    public ResponseEntity<List<TestCaseDTO>> searchTestCases(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TestCases for query {}", query);
        Page<TestCaseDTO> page = testCaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/test-cases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
