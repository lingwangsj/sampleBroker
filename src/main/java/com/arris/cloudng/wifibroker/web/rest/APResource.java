package com.arris.cloudng.wifibroker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arris.cloudng.wifibroker.domain.AP;
import com.arris.cloudng.wifibroker.service.APService;
import com.arris.cloudng.wifibroker.web.rest.errors.BadRequestAlertException;
import com.arris.cloudng.wifibroker.web.rest.util.HeaderUtil;
import com.arris.cloudng.wifibroker.web.rest.util.PaginationUtil;
import com.arris.cloudng.wifibroker.service.dto.APCriteria;
import com.arris.cloudng.wifibroker.service.APQueryService;
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

/**
 * REST controller for managing AP.
 */
@RestController
@RequestMapping("/api")
public class APResource {

    private final Logger log = LoggerFactory.getLogger(APResource.class);

    private static final String ENTITY_NAME = "aP";

    private final APService aPService;

    private final APQueryService aPQueryService;

    public APResource(APService aPService, APQueryService aPQueryService) {
        this.aPService = aPService;
        this.aPQueryService = aPQueryService;
    }

    /**
     * POST  /aps : Create a new aP.
     *
     * @param aP the aP to create
     * @return the ResponseEntity with status 201 (Created) and with body the new aP, or with status 400 (Bad Request) if the aP has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/aps")
    @Timed
    public ResponseEntity<AP> createAP(@Valid @RequestBody AP aP) throws URISyntaxException {
        log.debug("REST request to save AP : {}", aP);
        if (aP.getId() != null) {
            throw new BadRequestAlertException("A new aP cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AP result = aPService.save(aP);
        return ResponseEntity.created(new URI("/api/aps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /aps : Updates an existing aP.
     *
     * @param aP the aP to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated aP,
     * or with status 400 (Bad Request) if the aP is not valid,
     * or with status 500 (Internal Server Error) if the aP couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/aps")
    @Timed
    public ResponseEntity<AP> updateAP(@Valid @RequestBody AP aP) throws URISyntaxException {
        log.debug("REST request to update AP : {}", aP);
        if (aP.getId() == null) {
            return createAP(aP);
        }
        AP result = aPService.save(aP);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, aP.getId().toString()))
            .body(result);
    }

    /**
     * GET  /aps : get all the aPS.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of aPS in body
     */
    @GetMapping("/aps")
    @Timed
    public ResponseEntity<List<AP>> getAllAPS(APCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APS by criteria: {}", criteria);
        Page<AP> page = aPQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/aps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /aps/:id : get the "id" aP.
     *
     * @param id the id of the aP to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the aP, or with status 404 (Not Found)
     */
    @GetMapping("/aps/{id}")
    @Timed
    public ResponseEntity<AP> getAP(@PathVariable Long id) {
        log.debug("REST request to get AP : {}", id);
        AP aP = aPService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(aP));
    }

    /**
     * DELETE  /aps/:id : delete the "id" aP.
     *
     * @param id the id of the aP to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/aps/{id}")
    @Timed
    public ResponseEntity<Void> deleteAP(@PathVariable Long id) {
        log.debug("REST request to delete AP : {}", id);
        aPService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
