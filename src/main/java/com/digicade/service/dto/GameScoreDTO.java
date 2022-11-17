package com.digicade.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.GameScore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameScoreDTO implements Serializable {

    private Long id;

    private Integer score;

    private LocalDate date;

    private GameDTO game;

    private PlayerDTO player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        if (!(o instanceof GameScoreDTO)) {
            return false;
        }

        GameScoreDTO gameScoreDTO = (GameScoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameScoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameScoreDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", date='" + getDate() + "'" +
            ", game=" + getGame() +
            ", player=" + getPlayer() +
            "}";
    }
}
