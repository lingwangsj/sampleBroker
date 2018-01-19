package com.arris.cloudng.wifibroker.service;

import com.arris.cloudng.wifibroker.domain.Domain;
import com.arris.cloudng.wifibroker.repository.DomainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Domain.
 */
@Service
@Transactional
public class DomainService {

    private final Logger log = LoggerFactory.getLogger(DomainService.class);

    private final DomainRepository domainRepository;

    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    /**
     * Save a domain.
     *
     * @param domain the entity to save
     * @return the persisted entity
     */
    public Domain save(Domain domain) {
        log.debug("Request to save Domain : {}", domain);
        return domainRepository.save(domain);
    }

    /**
     * Get all the domains.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Domain> findAll() {
        log.debug("Request to get all Domains");
        return domainRepository.findAll();
    }

    /**
     * Get one domain by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Domain findOne(Long id) {
        log.debug("Request to get Domain : {}", id);
        return domainRepository.findOne(id);
    }

    /**
     * Delete the domain by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Domain : {}", id);
        domainRepository.delete(id);
    }
}
