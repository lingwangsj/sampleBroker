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

import com.arris.cloudng.wifibroker.domain.AP;
import com.arris.cloudng.wifibroker.domain.*; // for static metamodels
import com.arris.cloudng.wifibroker.repository.APRepository;
import com.arris.cloudng.wifibroker.service.dto.APCriteria;


/**
 * Service for executing complex queries for AP entities in the database.
 * The main input is a {@link APCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AP} or a {@link Page} of {@link AP} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APQueryService extends QueryService<AP> {

    private final Logger log = LoggerFactory.getLogger(APQueryService.class);


    private final APRepository aPRepository;

    public APQueryService(APRepository aPRepository) {
        this.aPRepository = aPRepository;
    }

    /**
     * Return a {@link List} of {@link AP} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AP> findByCriteria(APCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<AP> specification = createSpecification(criteria);
        return aPRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AP} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AP> findByCriteria(APCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<AP> specification = createSpecification(criteria);
        return aPRepository.findAll(specification, page);
    }

    /**
     * Function to convert APCriteria to a {@link Specifications}
     */
    private Specifications<AP> createSpecification(APCriteria criteria) {
        Specifications<AP> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AP_.id));
            }
            if (criteria.getServiceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceId(), AP_.serviceId));
            }
            if (criteria.getServiceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceName(), AP_.serviceName));
            }
            if (criteria.getApgroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getApgroupId(), AP_.apgroup, APGroup_.id));
            }
        }
        return specification;
    }

}
