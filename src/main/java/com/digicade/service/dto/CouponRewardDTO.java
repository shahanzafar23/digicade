package com.digicade.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.CouponReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponRewardDTO implements Serializable {

    private Long id;

    private String description;

    private String location;

    private Integer tix;

    private Integer comp;

    private LocalDate expiry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTix() {
        return tix;
    }

    public void setTix(Integer tix) {
        this.tix = tix;
    }

    public Integer getComp() {
        return comp;
    }

    public void setComp(Integer comp) {
        this.comp = comp;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CouponRewardDTO)) {
            return false;
        }

        CouponRewardDTO couponRewardDTO = (CouponRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, couponRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponRewardDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", tix=" + getTix() +
            ", comp=" + getComp() +
            ", expiry='" + getExpiry() + "'" +
            "}";
    }
}
