package com.arris.cloudng.wifibroker.service;

import com.arris.cloudng.wifibroker.domain.APGroup;
import com.arris.cloudng.wifibroker.repository.APGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing APGroup.
 */
@Service
@Transactional
public class APGroupService {

    private final Logger log = LoggerFactory.getLogger(APGroupService.class);

    private final APGroupRepository aPGroupRepository;

    public APGroupService(APGroupRepository aPGroupRepository) {
        this.aPGroupRepository = aPGroupRepository;
    }

    /**
     * Save a aPGroup.
     *
     * @param aPGroup the entity to save
     * @return the persisted entity
     */
    public APGroup save(APGroup aPGroup) {
        log.debug("Request to save APGroup : {}", aPGroup);
        return aPGroupRepository.save(aPGroup);
    }

    /**
     * Get all the aPGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<APGroup> findAll(Pageable pageable) {
        log.debug("Request to get all APGroups");
        return aPGroupRepository.findAll(pageable);
    }

    /**
     * Get one aPGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public APGroup findOne(Long id) {
        log.debug("Request to get APGroup : {}", id);
        return aPGroupRepository.findOne(id);
    }

    /**
     * Delete the aPGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete APGroup : {}", id);
        aPGroupRepository.delete(id);
    }
}
