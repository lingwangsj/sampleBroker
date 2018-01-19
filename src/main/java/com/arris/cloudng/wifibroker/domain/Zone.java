package com.arris.cloudng.wifibroker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Zone.
 */
@Entity
@Table(name = "zone")
public class Zone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "venue_id", nullable = false)
    private String venueId;

    @NotNull
    @Column(name = "zone_name", nullable = false)
    private String zoneName;

    @OneToMany(mappedBy = "zone")
    @JsonIgnore
    private Set<AP> aps = new HashSet<>();

    @OneToMany(mappedBy = "zone")
    @JsonIgnore
    private Set<WlanGroup> wlanGroups = new HashSet<>();

    @OneToMany(mappedBy = "zone")
    @JsonIgnore
    private Set<Wlan> wlans = new HashSet<>();

    @ManyToOne
    private Domain domain;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVenueId() {
        return venueId;
    }

    public Zone venueId(String venueId) {
        this.venueId = venueId;
        return this;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public Zone zoneName(String zoneName) {
        this.zoneName = zoneName;
        return this;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Set<AP> getAps() {
        return aps;
    }

    public Zone aps(Set<AP> aPS) {
        this.aps = aPS;
        return this;
    }

    public Zone addAp(AP aP) {
        this.aps.add(aP);
        aP.setZone(this);
        return this;
    }

    public Zone removeAp(AP aP) {
        this.aps.remove(aP);
        aP.setZone(null);
        return this;
    }

    public void setAps(Set<AP> aPS) {
        this.aps = aPS;
    }

    public Set<WlanGroup> getWlanGroups() {
        return wlanGroups;
    }

    public Zone wlanGroups(Set<WlanGroup> wlanGroups) {
        this.wlanGroups = wlanGroups;
        return this;
    }

    public Zone addWlanGroup(WlanGroup wlanGroup) {
        this.wlanGroups.add(wlanGroup);
        wlanGroup.setZone(this);
        return this;
    }

    public Zone removeWlanGroup(WlanGroup wlanGroup) {
        this.wlanGroups.remove(wlanGroup);
        wlanGroup.setZone(null);
        return this;
    }

    public void setWlanGroups(Set<WlanGroup> wlanGroups) {
        this.wlanGroups = wlanGroups;
    }

    public Set<Wlan> getWlans() {
        return wlans;
    }

    public Zone wlans(Set<Wlan> wlans) {
        this.wlans = wlans;
        return this;
    }

    public Zone addWlan(Wlan wlan) {
        this.wlans.add(wlan);
        wlan.setZone(this);
        return this;
    }

    public Zone removeWlan(Wlan wlan) {
        this.wlans.remove(wlan);
        wlan.setZone(null);
        return this;
    }

    public void setWlans(Set<Wlan> wlans) {
        this.wlans = wlans;
    }

    public Domain getDomain() {
        return domain;
    }

    public Zone domain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
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
        Zone zone = (Zone) o;
        if (zone.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), zone.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Zone{" +
            "id=" + getId() +
            ", venueId='" + getVenueId() + "'" +
            ", zoneName='" + getZoneName() + "'" +
            "}";
    }
}
