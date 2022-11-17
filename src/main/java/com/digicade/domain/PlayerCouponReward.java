package com.digicade.domain;

import com.digicade.domain.enumeration.CouponStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A PlayerCouponReward.
 */
@Entity
@Table(name = "player_coupon_reward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerCouponReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CouponStatus status;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "digiUser", "gameScores", "highScores", "gameBadges", "transactions", "playerCouponRewards", "playerNftRewards" },
        allowSetters = true
    )
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties(value = { "couponImages", "playerCouponRewards", "dailyRewards" }, allowSetters = true)
    private CouponReward couponReward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerCouponReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PlayerCouponReward date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CouponStatus getStatus() {
        return this.status;
    }

    public PlayerCouponReward status(CouponStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerCouponReward player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public CouponReward getCouponReward() {
        return this.couponReward;
    }

    public void setCouponReward(CouponReward couponReward) {
        this.couponReward = couponReward;
    }

    public PlayerCouponReward couponReward(CouponReward couponReward) {
        this.setCouponReward(couponReward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerCouponReward)) {
            return false;
        }
        return id != null && id.equals(((PlayerCouponReward) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerCouponReward{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
