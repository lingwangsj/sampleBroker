package com.arris.cloudng.wifibroker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Wlan.
 */
@Entity
@Table(name = "wlan")
public class Wlan implements Serializable {

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

    @ManyToOne
    private Zone zone;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<WlanGroup> groups = new HashSet<>();

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

    public Wlan serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Wlan deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Wlan serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Wlan deviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Zone getZone() {
        return zone;
    }

    public Wlan zone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Set<WlanGroup> getGroups() {
        return groups;
    }

    public Wlan groups(Set<WlanGroup> wlanGroups) {
        this.groups = wlanGroups;
        return this;
    }

    public Wlan addGroup(WlanGroup wlanGroup) {
        this.groups.add(wlanGroup);
        wlanGroup.getMembers().add(this);
        return this;
    }

    public Wlan removeGroup(WlanGroup wlanGroup) {
        this.groups.remove(wlanGroup);
        wlanGroup.getMembers().remove(this);
        return this;
    }

    public void setGroups(Set<WlanGroup> wlanGroups) {
        this.groups = wlanGroups;
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
        Wlan wlan = (Wlan) o;
        if (wlan.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wlan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wlan{" +
            "id=" + getId() +
            ", serviceId='" + getServiceId() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", serviceName='" + getServiceName() + "'" +
            ", deviceName='" + getDeviceName() + "'" +
            "}";
    }
}
