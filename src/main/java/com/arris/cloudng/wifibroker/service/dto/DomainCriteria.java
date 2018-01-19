package com.arris.cloudng.wifibroker.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Domain entity. This class is used in DomainResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /domains?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DomainCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter domainName;

    private LongFilter zoneId;

    public DomainCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getDomainName() {
        return domainName;
    }

    public void setDomainName(StringFilter domainName) {
        this.domainName = domainName;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return "DomainCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
                (domainName != null ? "domainName=" + domainName + ", " : "") +
                (zoneId != null ? "zoneId=" + zoneId + ", " : "") +
            "}";
    }

}
