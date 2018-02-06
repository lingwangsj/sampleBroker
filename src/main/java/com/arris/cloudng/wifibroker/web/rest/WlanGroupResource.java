package com.arris.cloudng.wifibroker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.service.WlanGroupService;
import com.arris.cloudng.wifibroker.web.rest.errors.BadRequestAlertException;
import com.arris.cloudng.wifibroker.web.rest.util.HeaderUtil;
import com.arris.cloudng.wifibroker.web.rest.util.PaginationUtil;
import com.arris.cloudng.wifibroker.service.dto.WlanGroupCriteria;
import com.arris.cloudng.wifibroker.service.WlanGroupQueryService;
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
 * REST controller for managing WlanGroup.
 */
@RestController
@RequestMapping("/api")
public class WlanGroupResource {

    private final Logger log = LoggerFactory.getLogger(WlanGroupResource.class);

    private static final String ENTITY_NAME = "wlanGroup";

    private final WlanGroupService wlanGroupService;

    private final WlanGroupQueryService wlanGroupQueryService;

    public WlanGroupResource(WlanGroupService wlanGroupService, WlanGroupQueryService wlanGroupQueryService) {
        this.wlanGroupService = wlanGroupService;
        this.wlanGroupQueryService = wlanGroupQueryService;
    }

    /**
     * POST  /wlan-groups : Create a new wlanGroup.
     *
     * @param wlanGroup the wlanGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wlanGroup, or with status 400 (Bad Request) if the wlanGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wlan-groups")
    @Timed
    public ResponseEntity<WlanGroup> createWlanGroup(@Valid @RequestBody WlanGroup wlanGroup) throws URISyntaxException {
        log.debug("REST request to save WlanGroup : {}", wlanGroup);
        if (wlanGroup.getId() != null) {
            throw new BadRequestAlertException("A new wlanGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WlanGroup result = wlanGroupService.save(wlanGroup);
        return ResponseEntity.created(new URI("/api/wlan-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wlan-groups : Updates an existing wlanGroup.
     *
     * @param wlanGroup the wlanGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wlanGroup,
     * or with status 400 (Bad Request) if the wlanGroup is not valid,
     * or with status 500 (Internal Server Error) if the wlanGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wlan-groups")
    @Timed
    public ResponseEntity<WlanGroup> updateWlanGroup(@Valid @RequestBody WlanGroup wlanGroup) throws URISyntaxException {
        log.debug("REST request to update WlanGroup : {}", wlanGroup);
        if (wlanGroup.getId() == null) {
            return createWlanGroup(wlanGroup);
        }
        WlanGroup result = wlanGroupService.save(wlanGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wlanGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wlan-groups : get all the wlanGroups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of wlanGroups in body
     */
    @GetMapping("/wlan-groups")
    @Timed
    public ResponseEntity<List<WlanGroup>> getAllWlanGroups(WlanGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WlanGroups by criteria: {}", criteria);
        Page<WlanGroup> page = wlanGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wlan-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wlan-groups/:id : get the "id" wlanGroup.
     *
     * @param id the id of the wlanGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wlanGroup, or with status 404 (Not Found)
     */
    @GetMapping("/wlan-groups/{id}")
    @Timed
    public ResponseEntity<WlanGroup> getWlanGroup(@PathVariable Long id) {
        log.debug("REST request to get WlanGroup : {}", id);
        WlanGroup wlanGroup = wlanGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wlanGroup));
    }

    /**
     * DELETE  /wlan-groups/:id : delete the "id" wlanGroup.
     *
     * @param id the id of the wlanGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wlan-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteWlanGroup(@PathVariable Long id) {
        log.debug("REST request to delete WlanGroup : {}", id);
        wlanGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
