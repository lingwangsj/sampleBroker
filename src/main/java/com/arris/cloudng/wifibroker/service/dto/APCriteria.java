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
 * Criteria class for the AP entity. This class is used in APResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /aps?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class APCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter serialNumber;

    private StringFilter apName;

    private LongFilter wg24Id;

    private LongFilter wg50Id;

    private LongFilter zoneId;

    public APCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StringFilter getApName() {
        return apName;
    }

    public void setApName(StringFilter apName) {
        this.apName = apName;
    }

    public LongFilter getWg24Id() {
        return wg24Id;
    }

    public void setWg24Id(LongFilter wg24Id) {
        this.wg24Id = wg24Id;
    }

    public LongFilter getWg50Id() {
        return wg50Id;
    }

    public void setWg50Id(LongFilter wg50Id) {
        this.wg50Id = wg50Id;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return "APCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
                (apName != null ? "apName=" + apName + ", " : "") +
                (wg24Id != null ? "wg24Id=" + wg24Id + ", " : "") +
                (wg50Id != null ? "wg50Id=" + wg50Id + ", " : "") +
                (zoneId != null ? "zoneId=" + zoneId + ", " : "") +
            "}";
    }

}
