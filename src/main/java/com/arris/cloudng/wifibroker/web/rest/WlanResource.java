package com.arris.cloudng.wifibroker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arris.cloudng.wifibroker.domain.Wlan;
import com.arris.cloudng.wifibroker.service.WlanService;
import com.arris.cloudng.wifibroker.web.rest.errors.BadRequestAlertException;
import com.arris.cloudng.wifibroker.web.rest.util.HeaderUtil;
import com.arris.cloudng.wifibroker.service.dto.WlanCriteria;
import com.arris.cloudng.wifibroker.service.WlanQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Wlan.
 */
@RestController
@RequestMapping("/api")
public class WlanResource {

    private final Logger log = LoggerFactory.getLogger(WlanResource.class);

    private static final String ENTITY_NAME = "wlan";

    private final WlanService wlanService;

    private final WlanQueryService wlanQueryService;

    public WlanResource(WlanService wlanService, WlanQueryService wlanQueryService) {
        this.wlanService = wlanService;
        this.wlanQueryService = wlanQueryService;
    }

    /**
     * POST  /wlans : Create a new wlan.
     *
     * @param wlan the wlan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wlan, or with status 400 (Bad Request) if the wlan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wlans")
    @Timed
    public ResponseEntity<Wlan> createWlan(@Valid @RequestBody Wlan wlan) throws URISyntaxException {
        log.debug("REST request to save Wlan : {}", wlan);
        if (wlan.getId() != null) {
            throw new BadRequestAlertException("A new wlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Wlan result = wlanService.save(wlan);
        return ResponseEntity.created(new URI("/api/wlans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wlans : Updates an existing wlan.
     *
     * @param wlan the wlan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wlan,
     * or with status 400 (Bad Request) if the wlan is not valid,
     * or with status 500 (Internal Server Error) if the wlan couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wlans")
    @Timed
    public ResponseEntity<Wlan> updateWlan(@Valid @RequestBody Wlan wlan) throws URISyntaxException {
        log.debug("REST request to update Wlan : {}", wlan);
        if (wlan.getId() == null) {
            return createWlan(wlan);
        }
        Wlan result = wlanService.save(wlan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wlan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wlans : get all the wlans.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of wlans in body
     */
    @GetMapping("/wlans")
    @Timed
    public ResponseEntity<List<Wlan>> getAllWlans(WlanCriteria criteria) {
        log.debug("REST request to get Wlans by criteria: {}", criteria);
        List<Wlan> entityList = wlanQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /wlans/:id : get the "id" wlan.
     *
     * @param id the id of the wlan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wlan, or with status 404 (Not Found)
     */
    @GetMapping("/wlans/{id}")
    @Timed
    public ResponseEntity<Wlan> getWlan(@PathVariable Long id) {
        log.debug("REST request to get Wlan : {}", id);
        Wlan wlan = wlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wlan));
    }

    /**
     * DELETE  /wlans/:id : delete the "id" wlan.
     *
     * @param id the id of the wlan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wlans/{id}")
    @Timed
    public ResponseEntity<Void> deleteWlan(@PathVariable Long id) {
        log.debug("REST request to delete Wlan : {}", id);
        wlanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
