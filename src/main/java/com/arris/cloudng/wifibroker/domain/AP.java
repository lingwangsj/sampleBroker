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
    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @ManyToOne
    private APGroup apgroup;

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

    public AP serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public AP serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public APGroup getApgroup() {
        return apgroup;
    }

    public AP apgroup(APGroup aPGroup) {
        this.apgroup = aPGroup;
        return this;
    }

    public void setApgroup(APGroup aPGroup) {
        this.apgroup = aPGroup;
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
            ", serviceId='" + getServiceId() + "'" +
            ", serviceName='" + getServiceName() + "'" +
            "}";
    }
}
