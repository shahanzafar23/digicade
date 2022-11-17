package com.digicade.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.PlayerNftReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerNftRewardDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private PlayerDTO player;

    private NftRewardDTO nftReward;

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

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
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
        if (!(o instanceof PlayerNftRewardDTO)) {
            return false;
        }

        PlayerNftRewardDTO playerNftRewardDTO = (PlayerNftRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerNftRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerNftRewardDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", player=" + getPlayer() +
            ", nftReward=" + getNftReward() +
            "}";
    }
}
