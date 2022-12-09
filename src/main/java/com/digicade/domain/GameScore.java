package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A GameScore.
 */
@Entity
@Table(name = "game_score")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "date")
    private LocalDate date;

    //"gameScores"
    @ManyToOne
    @JsonIgnoreProperties(value = { "highScores", "gameBadges", "gameLevels" }, allowSetters = true)
    private Game game;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "digiUser", "gameScores", "highScores", "gameBadges", "transactions", "playerCouponRewards", "playerNftRewards" },
        allowSetters = true
    )
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameScore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return this.score;
    }

    public GameScore score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public GameScore date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameScore game(Game game) {
        this.setGame(game);
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameScore player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameScore)) {
            return false;
        }
        return id != null && id.equals(((GameScore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameScore{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
