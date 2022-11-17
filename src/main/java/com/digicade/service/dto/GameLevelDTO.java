package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.GameLevel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameLevelDTO implements Serializable {

    private Long id;

    private Integer level;

    private Integer score;

    private GameDTO game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameLevelDTO)) {
            return false;
        }

        GameLevelDTO gameLevelDTO = (GameLevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameLevelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameLevelDTO{" +
            "id=" + getId() +
            ", level=" + getLevel() +
            ", score=" + getScore() +
            ", game=" + getGame() +
            "}";
    }
}
