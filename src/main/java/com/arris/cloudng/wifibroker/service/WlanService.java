package com.arris.cloudng.wifibroker.service;

import com.arris.cloudng.wifibroker.domain.Wlan;
import com.arris.cloudng.wifibroker.repository.WlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Wlan.
 */
@Service
@Transactional
public class WlanService {

    private final Logger log = LoggerFactory.getLogger(WlanService.class);

    private final WlanRepository wlanRepository;

    public WlanService(WlanRepository wlanRepository) {
        this.wlanRepository = wlanRepository;
    }

    /**
     * Save a wlan.
     *
     * @param wlan the entity to save
     * @return the persisted entity
     */
    public Wlan save(Wlan wlan) {
        log.debug("Request to save Wlan : {}", wlan);
        return wlanRepository.save(wlan);
    }

    /**
     * Get all the wlans.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Wlan> findAll() {
        log.debug("Request to get all Wlans");
        return wlanRepository.findAll();
    }

    /**
     * Get one wlan by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Wlan findOne(Long id) {
        log.debug("Request to get Wlan : {}", id);
        return wlanRepository.findOne(id);
    }

    /**
     * Delete the wlan by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Wlan : {}", id);
        wlanRepository.delete(id);
    }
}
