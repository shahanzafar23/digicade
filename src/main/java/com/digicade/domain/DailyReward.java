package com.digicade.domain;

import com.digicade.domain.enumeration.RewardType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A DailyReward.
 */
@Entity
@Table(name = "daily_reward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DailyReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time")
    private String time;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type")
    private RewardType rewardType;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "couponImages", "playerCouponRewards", "dailyRewards" }, allowSetters = true)
    private CouponReward couponReward;

    @ManyToOne
    @JsonIgnoreProperties(value = { "playerNftRewards", "dailyRewards" }, allowSetters = true)
    private NftReward nftReward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DailyReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public DailyReward time(String time) {
        this.setTime(time);
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public RewardType getRewardType() {
        return this.rewardType;
    }

    public DailyReward rewardType(RewardType rewardType) {
        this.setRewardType(rewardType);
        return this;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Double getAmount() {
        return this.amount;
    }

    public DailyReward amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CouponReward getCouponReward() {
        return this.couponReward;
    }

    public void setCouponReward(CouponReward couponReward) {
        this.couponReward = couponReward;
    }

    public DailyReward couponReward(CouponReward couponReward) {
        this.setCouponReward(couponReward);
        return this;
    }

    public NftReward getNftReward() {
        return this.nftReward;
    }

    public void setNftReward(NftReward nftReward) {
        this.nftReward = nftReward;
    }

    public DailyReward nftReward(NftReward nftReward) {
        this.setNftReward(nftReward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyReward)) {
            return false;
        }
        return id != null && id.equals(((DailyReward) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyReward{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", rewardType='" + getRewardType() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
