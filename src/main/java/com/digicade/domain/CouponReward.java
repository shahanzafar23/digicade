package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A CouponReward.
 */
@Entity
@Table(name = "coupon_reward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "tix")
    private Integer tix;

    @Column(name = "comp")
    private Integer comp;

    @Column(name = "expiry")
    private LocalDate expiry;

    @OneToMany(mappedBy = "couponReward")
    @JsonIgnoreProperties(value = { "couponReward" }, allowSetters = true)
    private Set<CouponImage> couponImages = new HashSet<>();

    @OneToMany(mappedBy = "couponReward")
    @JsonIgnoreProperties(value = { "player", "couponReward" }, allowSetters = true)
    private Set<PlayerCouponReward> playerCouponRewards = new HashSet<>();

    @OneToMany(mappedBy = "couponReward")
    @JsonIgnoreProperties(value = { "couponReward", "nftReward" }, allowSetters = true)
    private Set<DailyReward> dailyRewards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CouponReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public CouponReward description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public CouponReward location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTix() {
        return this.tix;
    }

    public CouponReward tix(Integer tix) {
        this.setTix(tix);
        return this;
    }

    public void setTix(Integer tix) {
        this.tix = tix;
    }

    public Integer getComp() {
        return this.comp;
    }

    public CouponReward comp(Integer comp) {
        this.setComp(comp);
        return this;
    }

    public void setComp(Integer comp) {
        this.comp = comp;
    }

    public LocalDate getExpiry() {
        return this.expiry;
    }

    public CouponReward expiry(LocalDate expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public Set<CouponImage> getCouponImages() {
        return this.couponImages;
    }

    public void setCouponImages(Set<CouponImage> couponImages) {
        if (this.couponImages != null) {
            this.couponImages.forEach(i -> i.setCouponReward(null));
        }
        if (couponImages != null) {
            couponImages.forEach(i -> i.setCouponReward(this));
        }
        this.couponImages = couponImages;
    }

    public CouponReward couponImages(Set<CouponImage> couponImages) {
        this.setCouponImages(couponImages);
        return this;
    }

    public CouponReward addCouponImage(CouponImage couponImage) {
        this.couponImages.add(couponImage);
        couponImage.setCouponReward(this);
        return this;
    }

    public CouponReward removeCouponImage(CouponImage couponImage) {
        this.couponImages.remove(couponImage);
        couponImage.setCouponReward(null);
        return this;
    }

    public Set<PlayerCouponReward> getPlayerCouponRewards() {
        return this.playerCouponRewards;
    }

    public void setPlayerCouponRewards(Set<PlayerCouponReward> playerCouponRewards) {
        if (this.playerCouponRewards != null) {
            this.playerCouponRewards.forEach(i -> i.setCouponReward(null));
        }
        if (playerCouponRewards != null) {
            playerCouponRewards.forEach(i -> i.setCouponReward(this));
        }
        this.playerCouponRewards = playerCouponRewards;
    }

    public CouponReward playerCouponRewards(Set<PlayerCouponReward> playerCouponRewards) {
        this.setPlayerCouponRewards(playerCouponRewards);
        return this;
    }

    public CouponReward addPlayerCouponReward(PlayerCouponReward playerCouponReward) {
        this.playerCouponRewards.add(playerCouponReward);
        playerCouponReward.setCouponReward(this);
        return this;
    }

    public CouponReward removePlayerCouponReward(PlayerCouponReward playerCouponReward) {
        this.playerCouponRewards.remove(playerCouponReward);
        playerCouponReward.setCouponReward(null);
        return this;
    }

    public Set<DailyReward> getDailyRewards() {
        return this.dailyRewards;
    }

    public void setDailyRewards(Set<DailyReward> dailyRewards) {
        if (this.dailyRewards != null) {
            this.dailyRewards.forEach(i -> i.setCouponReward(null));
        }
        if (dailyRewards != null) {
            dailyRewards.forEach(i -> i.setCouponReward(this));
        }
        this.dailyRewards = dailyRewards;
    }

    public CouponReward dailyRewards(Set<DailyReward> dailyRewards) {
        this.setDailyRewards(dailyRewards);
        return this;
    }

    public CouponReward addDailyReward(DailyReward dailyReward) {
        this.dailyRewards.add(dailyReward);
        dailyReward.setCouponReward(this);
        return this;
    }

    public CouponReward removeDailyReward(DailyReward dailyReward) {
        this.dailyRewards.remove(dailyReward);
        dailyReward.setCouponReward(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CouponReward)) {
            return false;
        }
        return id != null && id.equals(((CouponReward) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponReward{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", tix=" + getTix() +
            ", comp=" + getComp() +
            ", expiry='" + getExpiry() + "'" +
            "}";
    }
}
