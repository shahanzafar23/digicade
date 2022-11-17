package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.Player} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerDTO implements Serializable {

    private Long id;

    private Integer gamePlayCredits;

    private Integer tix;

    private Integer comp;

    private Integer level;

    private String walletAddress;

    private DigiUserDTO digiUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGamePlayCredits() {
        return gamePlayCredits;
    }

    public void setGamePlayCredits(Integer gamePlayCredits) {
        this.gamePlayCredits = gamePlayCredits;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public DigiUserDTO getDigiUser() {
        return digiUser;
    }

    public void setDigiUser(DigiUserDTO digiUser) {
        this.digiUser = digiUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerDTO)) {
            return false;
        }

        PlayerDTO playerDTO = (PlayerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDTO{" +
            "id=" + getId() +
            ", gamePlayCredits=" + getGamePlayCredits() +
            ", tix=" + getTix() +
            ", comp=" + getComp() +
            ", level=" + getLevel() +
            ", walletAddress='" + getWalletAddress() + "'" +
            ", digiUser=" + getDigiUser() +
            "}";
    }
}
