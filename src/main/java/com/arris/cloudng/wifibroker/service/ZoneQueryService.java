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

import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.domain.*; // for static metamodels
import com.arris.cloudng.wifibroker.repository.ZoneRepository;
import com.arris.cloudng.wifibroker.service.dto.ZoneCriteria;


/**
 * Service for executing complex queries for Zone entities in the database.
 * The main input is a {@link ZoneCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Zone} or a {@link Page} of {@link Zone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ZoneQueryService extends QueryService<Zone> {

    private final Logger log = LoggerFactory.getLogger(ZoneQueryService.class);


    private final ZoneRepository zoneRepository;

    public ZoneQueryService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    /**
     * Return a {@link List} of {@link Zone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Zone> findByCriteria(ZoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Zone> specification = createSpecification(criteria);
        return zoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Zone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Zone> findByCriteria(ZoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Zone> specification = createSpecification(criteria);
        return zoneRepository.findAll(specification, page);
    }

    /**
     * Function to convert ZoneCriteria to a {@link Specifications}
     */
    private Specifications<Zone> createSpecification(ZoneCriteria criteria) {
        Specifications<Zone> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Zone_.id));
            }
            if (criteria.getVenueId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVenueId(), Zone_.venueId));
            }
            if (criteria.getZoneName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZoneName(), Zone_.zoneName));
            }
            if (criteria.getApId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getApId(), Zone_.aps, AP_.id));
            }
            if (criteria.getWlanGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWlanGroupId(), Zone_.wlanGroups, WlanGroup_.id));
            }
            if (criteria.getWlanId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWlanId(), Zone_.wlans, Wlan_.id));
            }
            if (criteria.getDomainId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDomainId(), Zone_.domain, Domain_.id));
            }
        }
        return specification;
    }

}
