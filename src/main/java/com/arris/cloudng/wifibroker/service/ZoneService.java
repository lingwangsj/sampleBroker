package com.arris.cloudng.wifibroker.service;

import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.repository.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Zone.
 */
@Service
@Transactional
public class ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneService.class);

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    /**
     * Save a zone.
     *
     * @param zone the entity to save
     * @return the persisted entity
     */
    public Zone save(Zone zone) {
        log.debug("Request to save Zone : {}", zone);
        return zoneRepository.save(zone);
    }

    /**
     * Get all the zones.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Zone> findAll(Pageable pageable) {
        log.debug("Request to get all Zones");
        return zoneRepository.findAll(pageable);
    }

    /**
     * Get one zone by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Zone findOne(Long id) {
        log.debug("Request to get Zone : {}", id);
        return zoneRepository.findOne(id);
    }

    /**
     * Delete the zone by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Zone : {}", id);
        zoneRepository.delete(id);
    }
}
