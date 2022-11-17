package com.digicade.service.dto;

import com.digicade.domain.enumeration.CouponStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.PlayerCouponReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerCouponRewardDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private CouponStatus status;

    private PlayerDTO player;

    private CouponRewardDTO couponReward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
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
        if (!(o instanceof PlayerCouponRewardDTO)) {
            return false;
        }

        PlayerCouponRewardDTO playerCouponRewardDTO = (PlayerCouponRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerCouponRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerCouponRewardDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", player=" + getPlayer() +
            ", couponReward=" + getCouponReward() +
            "}";
    }
}
