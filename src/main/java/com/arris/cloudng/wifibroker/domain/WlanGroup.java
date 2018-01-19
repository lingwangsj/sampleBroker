package com.arris.cloudng.wifibroker.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WlanGroup.
 */
@Entity
@Table(name = "wlan_group")
public class WlanGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @ManyToMany
    @JoinTable(name = "wlan_group_members",
               joinColumns = @JoinColumn(name="wlan_groups_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="id"))
    private Set<Wlan> members = new HashSet<>();

    @ManyToOne
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public WlanGroup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Wlan> getMembers() {
        return members;
    }

    public WlanGroup members(Set<Wlan> wlans) {
        this.members = wlans;
        return this;
    }

    public WlanGroup addMembers(Wlan wlan) {
        this.members.add(wlan);
        wlan.getGroups().add(this);
        return this;
    }

    public WlanGroup removeMembers(Wlan wlan) {
        this.members.remove(wlan);
        wlan.getGroups().remove(this);
        return this;
    }

    public void setMembers(Set<Wlan> wlans) {
        this.members = wlans;
    }

    public Zone getZone() {
        return zone;
    }

    public WlanGroup zone(Zone zone) {
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
        WlanGroup wlanGroup = (WlanGroup) o;
        if (wlanGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wlanGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WlanGroup{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
