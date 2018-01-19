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
    @Column(name = "network_id", nullable = false)
    private String networkId;

    @NotNull
    @Column(name = "wlan_name", nullable = false)
    private String wlanName;

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

    public String getNetworkId() {
        return networkId;
    }

    public Wlan networkId(String networkId) {
        this.networkId = networkId;
        return this;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getWlanName() {
        return wlanName;
    }

    public Wlan wlanName(String wlanName) {
        this.wlanName = wlanName;
        return this;
    }

    public void setWlanName(String wlanName) {
        this.wlanName = wlanName;
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
            ", networkId='" + getNetworkId() + "'" +
            ", wlanName='" + getWlanName() + "'" +
            "}";
    }
}
