package com.arris.cloudng.wifibroker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.service.ZoneService;
import com.arris.cloudng.wifibroker.web.rest.errors.BadRequestAlertException;
import com.arris.cloudng.wifibroker.web.rest.util.HeaderUtil;
import com.arris.cloudng.wifibroker.web.rest.util.PaginationUtil;
import com.arris.cloudng.wifibroker.service.dto.ZoneCriteria;
import com.arris.cloudng.wifibroker.service.ZoneQueryService;
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
 * REST controller for managing Zone.
 */
@RestController
@RequestMapping("/api")
public class ZoneResource {

    private final Logger log = LoggerFactory.getLogger(ZoneResource.class);

    private static final String ENTITY_NAME = "zone";

    private final ZoneService zoneService;

    private final ZoneQueryService zoneQueryService;

    public ZoneResource(ZoneService zoneService, ZoneQueryService zoneQueryService) {
        this.zoneService = zoneService;
        this.zoneQueryService = zoneQueryService;
    }

    /**
     * POST  /zones : Create a new zone.
     *
     * @param zone the zone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new zone, or with status 400 (Bad Request) if the zone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/zones")
    @Timed
    public ResponseEntity<Zone> createZone(@Valid @RequestBody Zone zone) throws URISyntaxException {
        log.debug("REST request to save Zone : {}", zone);
        if (zone.getId() != null) {
            throw new BadRequestAlertException("A new zone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Zone result = zoneService.save(zone);
        return ResponseEntity.created(new URI("/api/zones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /zones : Updates an existing zone.
     *
     * @param zone the zone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated zone,
     * or with status 400 (Bad Request) if the zone is not valid,
     * or with status 500 (Internal Server Error) if the zone couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/zones")
    @Timed
    public ResponseEntity<Zone> updateZone(@Valid @RequestBody Zone zone) throws URISyntaxException {
        log.debug("REST request to update Zone : {}", zone);
        if (zone.getId() == null) {
            return createZone(zone);
        }
        Zone result = zoneService.save(zone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, zone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /zones : get all the zones.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of zones in body
     */
    @GetMapping("/zones")
    @Timed
    public ResponseEntity<List<Zone>> getAllZones(ZoneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Zones by criteria: {}", criteria);
        Page<Zone> page = zoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/zones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /zones/:id : get the "id" zone.
     *
     * @param id the id of the zone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the zone, or with status 404 (Not Found)
     */
    @GetMapping("/zones/{id}")
    @Timed
    public ResponseEntity<Zone> getZone(@PathVariable Long id) {
        log.debug("REST request to get Zone : {}", id);
        Zone zone = zoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(zone));
    }

    /**
     * DELETE  /zones/:id : delete the "id" zone.
     *
     * @param id the id of the zone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/zones/{id}")
    @Timed
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        log.debug("REST request to delete Zone : {}", id);
        zoneService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
