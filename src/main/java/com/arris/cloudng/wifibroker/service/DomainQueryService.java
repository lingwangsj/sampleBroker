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

import com.arris.cloudng.wifibroker.domain.Domain;
import com.arris.cloudng.wifibroker.domain.*; // for static metamodels
import com.arris.cloudng.wifibroker.repository.DomainRepository;
import com.arris.cloudng.wifibroker.service.dto.DomainCriteria;


/**
 * Service for executing complex queries for Domain entities in the database.
 * The main input is a {@link DomainCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Domain} or a {@link Page} of {@link Domain} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DomainQueryService extends QueryService<Domain> {

    private final Logger log = LoggerFactory.getLogger(DomainQueryService.class);


    private final DomainRepository domainRepository;

    public DomainQueryService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    /**
     * Return a {@link List} of {@link Domain} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Domain> findByCriteria(DomainCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Domain> specification = createSpecification(criteria);
        return domainRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Domain} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Domain> findByCriteria(DomainCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Domain> specification = createSpecification(criteria);
        return domainRepository.findAll(specification, page);
    }

    /**
     * Function to convert DomainCriteria to a {@link Specifications}
     */
    private Specifications<Domain> createSpecification(DomainCriteria criteria) {
        Specifications<Domain> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Domain_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), Domain_.tenantId));
            }
            if (criteria.getDomainName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDomainName(), Domain_.domainName));
            }
            if (criteria.getZoneId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getZoneId(), Domain_.zones, Zone_.id));
            }
        }
        return specification;
    }

}
