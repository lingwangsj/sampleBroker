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
 * Criteria class for the Wlan entity. This class is used in WlanResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /wlans?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WlanCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter networkId;

    private StringFilter wlanName;

    private LongFilter zoneId;

    private LongFilter groupId;

    public WlanCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNetworkId() {
        return networkId;
    }

    public void setNetworkId(StringFilter networkId) {
        this.networkId = networkId;
    }

    public StringFilter getWlanName() {
        return wlanName;
    }

    public void setWlanName(StringFilter wlanName) {
        this.wlanName = wlanName;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "WlanCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (networkId != null ? "networkId=" + networkId + ", " : "") +
                (wlanName != null ? "wlanName=" + wlanName + ", " : "") +
                (zoneId != null ? "zoneId=" + zoneId + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }

}
