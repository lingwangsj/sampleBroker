package com.arris.cloudng.wifibroker.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.arris.cloudng.wifibroker.domain.APGroup;
import com.arris.cloudng.wifibroker.domain.*; // for static metamodels
import com.arris.cloudng.wifibroker.repository.APGroupRepository;
import com.arris.cloudng.wifibroker.service.dto.APGroupCriteria;


/**
 * Service for executing complex queries for APGroup entities in the database.
 * The main input is a {@link APGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGroup} or a {@link Page} of {@link APGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGroupQueryService extends QueryService<APGroup> {

    private final Logger log = LoggerFactory.getLogger(APGroupQueryService.class);


    private final APGroupRepository aPGroupRepository;

    public APGroupQueryService(APGroupRepository aPGroupRepository) {
        this.aPGroupRepository = aPGroupRepository;
    }

    /**
     * Return a {@link List} of {@link APGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGroup> findByCriteria(APGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<APGroup> specification = createSpecification(criteria);
        return aPGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGroup> findByCriteria(APGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<APGroup> specification = createSpecification(criteria);
        return aPGroupRepository.findAll(specification, page);
    }

    /**
     * Function to convert APGroupCriteria to a {@link Specifications}
     */
    private Specifications<APGroup> createSpecification(APGroupCriteria criteria) {
        Specifications<APGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), APGroup_.id));
            }
            if (criteria.getServiceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceId(), APGroup_.serviceId));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceId(), APGroup_.deviceId));
            }
            if (criteria.getServiceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceName(), APGroup_.serviceName));
            }
            if (criteria.getDeviceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceName(), APGroup_.deviceName));
            }
            if (criteria.getWg24Id() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWg24Id(), APGroup_.wg24, WlanGroup_.id));
            }
            if (criteria.getWg50Id() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWg50Id(), APGroup_.wg50, WlanGroup_.id));
            }
            if (criteria.getApId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getApId(), APGroup_.aps, AP_.id));
            }
            if (criteria.getZoneId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getZoneId(), APGroup_.zone, Zone_.id));
            }
        }
        return specification;
    }

}
