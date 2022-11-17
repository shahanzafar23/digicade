package com.digicade.service.dto;

import com.digicade.domain.enumeration.RewardType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.DailyReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DailyRewardDTO implements Serializable {

    private Long id;

    private String time;

    private RewardType rewardType;

    private Double amount;

    private CouponRewardDTO couponReward;

    private NftRewardDTO nftReward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CouponRewardDTO getCouponReward() {
        return couponReward;
    }

    public void setCouponReward(CouponRewardDTO couponReward) {
        this.couponReward = couponReward;
    }

    public NftRewardDTO getNftReward() {
        return nftReward;
    }

    public void setNftReward(NftRewardDTO nftReward) {
        this.nftReward = nftReward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyRewardDTO)) {
            return false;
        }

        DailyRewardDTO dailyRewardDTO = (DailyRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dailyRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyRewardDTO{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", rewardType='" + getRewardType() + "'" +
            ", amount=" + getAmount() +
            ", couponReward=" + getCouponReward() +
            ", nftReward=" + getNftReward() +
            "}";
    }
}
