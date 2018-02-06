package com.arris.cloudng.wifibroker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arris.cloudng.wifibroker.domain.APGroup;
import com.arris.cloudng.wifibroker.service.APGroupService;
import com.arris.cloudng.wifibroker.web.rest.errors.BadRequestAlertException;
import com.arris.cloudng.wifibroker.web.rest.util.HeaderUtil;
import com.arris.cloudng.wifibroker.web.rest.util.PaginationUtil;
import com.arris.cloudng.wifibroker.service.dto.APGroupCriteria;
import com.arris.cloudng.wifibroker.service.APGroupQueryService;
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
 * REST controller for managing APGroup.
 */
@RestController
@RequestMapping("/api")
public class APGroupResource {

    private final Logger log = LoggerFactory.getLogger(APGroupResource.class);

    private static final String ENTITY_NAME = "aPGroup";

    private final APGroupService aPGroupService;

    private final APGroupQueryService aPGroupQueryService;

    public APGroupResource(APGroupService aPGroupService, APGroupQueryService aPGroupQueryService) {
        this.aPGroupService = aPGroupService;
        this.aPGroupQueryService = aPGroupQueryService;
    }

    /**
     * POST  /ap-groups : Create a new aPGroup.
     *
     * @param aPGroup the aPGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new aPGroup, or with status 400 (Bad Request) if the aPGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ap-groups")
    @Timed
    public ResponseEntity<APGroup> createAPGroup(@Valid @RequestBody APGroup aPGroup) throws URISyntaxException {
        log.debug("REST request to save APGroup : {}", aPGroup);
        if (aPGroup.getId() != null) {
            throw new BadRequestAlertException("A new aPGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGroup result = aPGroupService.save(aPGroup);
        return ResponseEntity.created(new URI("/api/ap-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ap-groups : Updates an existing aPGroup.
     *
     * @param aPGroup the aPGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated aPGroup,
     * or with status 400 (Bad Request) if the aPGroup is not valid,
     * or with status 500 (Internal Server Error) if the aPGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ap-groups")
    @Timed
    public ResponseEntity<APGroup> updateAPGroup(@Valid @RequestBody APGroup aPGroup) throws URISyntaxException {
        log.debug("REST request to update APGroup : {}", aPGroup);
        if (aPGroup.getId() == null) {
            return createAPGroup(aPGroup);
        }
        APGroup result = aPGroupService.save(aPGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, aPGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ap-groups : get all the aPGroups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of aPGroups in body
     */
    @GetMapping("/ap-groups")
    @Timed
    public ResponseEntity<List<APGroup>> getAllAPGroups(APGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGroups by criteria: {}", criteria);
        Page<APGroup> page = aPGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ap-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ap-groups/:id : get the "id" aPGroup.
     *
     * @param id the id of the aPGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the aPGroup, or with status 404 (Not Found)
     */
    @GetMapping("/ap-groups/{id}")
    @Timed
    public ResponseEntity<APGroup> getAPGroup(@PathVariable Long id) {
        log.debug("REST request to get APGroup : {}", id);
        APGroup aPGroup = aPGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(aPGroup));
    }

    /**
     * DELETE  /ap-groups/:id : delete the "id" aPGroup.
     *
     * @param id the id of the aPGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ap-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteAPGroup(@PathVariable Long id) {
        log.debug("REST request to delete APGroup : {}", id);
        aPGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
