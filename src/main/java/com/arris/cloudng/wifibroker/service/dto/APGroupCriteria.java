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
 * Criteria class for the APGroup entity. This class is used in APGroupResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /ap-groups?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class APGroupCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter serviceId;

    private StringFilter deviceId;

    private StringFilter serviceName;

    private StringFilter deviceName;

    private LongFilter wg24Id;

    private LongFilter wg50Id;

    private LongFilter apId;

    private LongFilter zoneId;

    public APGroupCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getServiceId() {
        return serviceId;
    }

    public void setServiceId(StringFilter serviceId) {
        this.serviceId = serviceId;
    }

    public StringFilter getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(StringFilter deviceId) {
        this.deviceId = deviceId;
    }

    public StringFilter getServiceName() {
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
    }

    public StringFilter getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(StringFilter deviceName) {
        this.deviceName = deviceName;
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

    public LongFilter getApId() {
        return apId;
    }

    public void setApId(LongFilter apId) {
        this.apId = apId;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return "APGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (serviceId != null ? "serviceId=" + serviceId + ", " : "") +
                (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
                (serviceName != null ? "serviceName=" + serviceName + ", " : "") +
                (deviceName != null ? "deviceName=" + deviceName + ", " : "") +
                (wg24Id != null ? "wg24Id=" + wg24Id + ", " : "") +
                (wg50Id != null ? "wg50Id=" + wg50Id + ", " : "") +
                (apId != null ? "apId=" + apId + ", " : "") +
                (zoneId != null ? "zoneId=" + zoneId + ", " : "") +
            "}";
    }

}
