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

import com.arris.cloudng.wifibroker.domain.Wlan;
import com.arris.cloudng.wifibroker.domain.*; // for static metamodels
import com.arris.cloudng.wifibroker.repository.WlanRepository;
import com.arris.cloudng.wifibroker.service.dto.WlanCriteria;


/**
 * Service for executing complex queries for Wlan entities in the database.
 * The main input is a {@link WlanCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Wlan} or a {@link Page} of {@link Wlan} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WlanQueryService extends QueryService<Wlan> {

    private final Logger log = LoggerFactory.getLogger(WlanQueryService.class);


    private final WlanRepository wlanRepository;

    public WlanQueryService(WlanRepository wlanRepository) {
        this.wlanRepository = wlanRepository;
    }

    /**
     * Return a {@link List} of {@link Wlan} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Wlan> findByCriteria(WlanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Wlan> specification = createSpecification(criteria);
        return wlanRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Wlan} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Wlan> findByCriteria(WlanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Wlan> specification = createSpecification(criteria);
        return wlanRepository.findAll(specification, page);
    }

    /**
     * Function to convert WlanCriteria to a {@link Specifications}
     */
    private Specifications<Wlan> createSpecification(WlanCriteria criteria) {
        Specifications<Wlan> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Wlan_.id));
            }
            if (criteria.getNetworkId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNetworkId(), Wlan_.networkId));
            }
            if (criteria.getWlanName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWlanName(), Wlan_.wlanName));
            }
            if (criteria.getZoneId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getZoneId(), Wlan_.zone, Zone_.id));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGroupId(), Wlan_.groups, WlanGroup_.id));
            }
        }
        return specification;
    }

}
