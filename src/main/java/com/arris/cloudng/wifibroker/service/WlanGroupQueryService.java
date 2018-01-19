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

import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.domain.*; // for static metamodels
import com.arris.cloudng.wifibroker.repository.WlanGroupRepository;
import com.arris.cloudng.wifibroker.service.dto.WlanGroupCriteria;


/**
 * Service for executing complex queries for WlanGroup entities in the database.
 * The main input is a {@link WlanGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WlanGroup} or a {@link Page} of {@link WlanGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WlanGroupQueryService extends QueryService<WlanGroup> {

    private final Logger log = LoggerFactory.getLogger(WlanGroupQueryService.class);


    private final WlanGroupRepository wlanGroupRepository;

    public WlanGroupQueryService(WlanGroupRepository wlanGroupRepository) {
        this.wlanGroupRepository = wlanGroupRepository;
    }

    /**
     * Return a {@link List} of {@link WlanGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WlanGroup> findByCriteria(WlanGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<WlanGroup> specification = createSpecification(criteria);
        return wlanGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WlanGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WlanGroup> findByCriteria(WlanGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<WlanGroup> specification = createSpecification(criteria);
        return wlanGroupRepository.findAll(specification, page);
    }

    /**
     * Function to convert WlanGroupCriteria to a {@link Specifications}
     */
    private Specifications<WlanGroup> createSpecification(WlanGroupCriteria criteria) {
        Specifications<WlanGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WlanGroup_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), WlanGroup_.groupName));
            }
            if (criteria.getMembersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMembersId(), WlanGroup_.members, Wlan_.id));
            }
            if (criteria.getZoneId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getZoneId(), WlanGroup_.zone, Zone_.id));
            }
        }
        return specification;
    }

}
