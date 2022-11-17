package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.HighScore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HighScoreDTO implements Serializable {

    private Long id;

    private Integer highestScore;

    private GameDTO game;

    private PlayerDTO player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
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
        if (!(o instanceof HighScoreDTO)) {
            return false;
        }

        HighScoreDTO highScoreDTO = (HighScoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, highScoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HighScoreDTO{" +
            "id=" + getId() +
            ", highestScore=" + getHighestScore() +
            ", game=" + getGame() +
            ", player=" + getPlayer() +
            "}";
    }
}
