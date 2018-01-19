package com.arris.cloudng.wifibroker.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AP.
 */
@Entity
@Table(name = "ap")
public class AP implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @NotNull
    @Column(name = "ap_name", nullable = false)
    private String apName;

    @OneToOne
    @JoinColumn(unique = true)
    private WlanGroup wg24;

    @OneToOne
    @JoinColumn(unique = true)
    private WlanGroup wg50;

    @ManyToOne
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public AP serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getApName() {
        return apName;
    }

    public AP apName(String apName) {
        this.apName = apName;
        return this;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public WlanGroup getWg24() {
        return wg24;
    }

    public AP wg24(WlanGroup wlanGroup) {
        this.wg24 = wlanGroup;
        return this;
    }

    public void setWg24(WlanGroup wlanGroup) {
        this.wg24 = wlanGroup;
    }

    public WlanGroup getWg50() {
        return wg50;
    }

    public AP wg50(WlanGroup wlanGroup) {
        this.wg50 = wlanGroup;
        return this;
    }

    public void setWg50(WlanGroup wlanGroup) {
        this.wg50 = wlanGroup;
    }

    public Zone getZone() {
        return zone;
    }

    public AP zone(Zone zone) {
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
        AP aP = (AP) o;
        if (aP.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), aP.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AP{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", apName='" + getApName() + "'" +
            "}";
    }
}
