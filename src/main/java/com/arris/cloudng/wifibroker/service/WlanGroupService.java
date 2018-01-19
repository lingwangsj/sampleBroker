package com.arris.cloudng.wifibroker.service;

import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.repository.WlanGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing WlanGroup.
 */
@Service
@Transactional
public class WlanGroupService {

    private final Logger log = LoggerFactory.getLogger(WlanGroupService.class);

    private final WlanGroupRepository wlanGroupRepository;

    public WlanGroupService(WlanGroupRepository wlanGroupRepository) {
        this.wlanGroupRepository = wlanGroupRepository;
    }

    /**
     * Save a wlanGroup.
     *
     * @param wlanGroup the entity to save
     * @return the persisted entity
     */
    public WlanGroup save(WlanGroup wlanGroup) {
        log.debug("Request to save WlanGroup : {}", wlanGroup);
        return wlanGroupRepository.save(wlanGroup);
    }

    /**
     * Get all the wlanGroups.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WlanGroup> findAll() {
        log.debug("Request to get all WlanGroups");
        return wlanGroupRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one wlanGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WlanGroup findOne(Long id) {
        log.debug("Request to get WlanGroup : {}", id);
        return wlanGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the wlanGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WlanGroup : {}", id);
        wlanGroupRepository.delete(id);
    }
}
