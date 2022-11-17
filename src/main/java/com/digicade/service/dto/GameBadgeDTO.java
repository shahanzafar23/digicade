package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.GameBadge} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameBadgeDTO implements Serializable {

    private Long id;

    private String logoUrl;

    private GameDTO game;

    private PlayerDTO player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameBadgeDTO)) {
            return false;
        }

        GameBadgeDTO gameBadgeDTO = (GameBadgeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameBadgeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameBadgeDTO{" +
            "id=" + getId() +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", game=" + getGame() +
            ", player=" + getPlayer() +
            "}";
    }
}
