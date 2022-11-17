package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.CouponImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponImageDTO implements Serializable {

    private Long id;

    private String imageUrl;

    private CouponRewardDTO couponReward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CouponRewardDTO getCouponReward() {
        return couponReward;
    }

    public void setCouponReward(CouponRewardDTO couponReward) {
        this.couponReward = couponReward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CouponImageDTO)) {
            return false;
        }

        CouponImageDTO couponImageDTO = (CouponImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, couponImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponImageDTO{" +
            "id=" + getId() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", couponReward=" + getCouponReward() +
            "}";
    }
}
