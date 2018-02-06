package com.arris.cloudng.wifibroker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A APGroup.
 */
@Entity
@Table(name = "ap_group")
public class APGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @NotNull
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @NotNull
    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @OneToOne
    @JoinColumn(unique = true)
    private WlanGroup wg24;

    @OneToOne
    @JoinColumn(unique = true)
    private WlanGroup wg50;

    @OneToMany(mappedBy = "apgroup")
    @JsonIgnore
    private Set<AP> aps = new HashSet<>();

    @ManyToOne
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public APGroup serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public APGroup deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public APGroup serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public APGroup deviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public WlanGroup getWg24() {
        return wg24;
    }

    public APGroup wg24(WlanGroup wlanGroup) {
        this.wg24 = wlanGroup;
        return this;
    }

    public void setWg24(WlanGroup wlanGroup) {
        this.wg24 = wlanGroup;
    }

    public WlanGroup getWg50() {
        return wg50;
    }

    public APGroup wg50(WlanGroup wlanGroup) {
        this.wg50 = wlanGroup;
        return this;
    }

    public void setWg50(WlanGroup wlanGroup) {
        this.wg50 = wlanGroup;
    }

    public Set<AP> getAps() {
        return aps;
    }

    public APGroup aps(Set<AP> aPS) {
        this.aps = aPS;
        return this;
    }

    public APGroup addAp(AP aP) {
        this.aps.add(aP);
        aP.setApgroup(this);
        return this;
    }

    public APGroup removeAp(AP aP) {
        this.aps.remove(aP);
        aP.setApgroup(null);
        return this;
    }

    public void setAps(Set<AP> aPS) {
        this.aps = aPS;
    }

    public Zone getZone() {
        return zone;
    }

    public APGroup zone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        APGroup aPGroup = (APGroup) o;
        if (aPGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), aPGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "APGroup{" +
            "id=" + getId() +
            ", serviceId='" + getServiceId() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", serviceName='" + getServiceName() + "'" +
            ", deviceName='" + getDeviceName() + "'" +
            "}";
    }
}
