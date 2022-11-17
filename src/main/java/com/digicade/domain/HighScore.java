package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A HighScore.
 */
@Entity
@Table(name = "high_score")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HighScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "highest_score")
    private Integer highestScore;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gameScores", "highScores", "gameBadges", "gameLevels" }, allowSetters = true)
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

    public HighScore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHighestScore() {
        return this.highestScore;
    }

    public HighScore highestScore(Integer highestScore) {
        this.setHighestScore(highestScore);
        return this;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public HighScore game(Game game) {
        this.setGame(game);
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public HighScore player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HighScore)) {
            return false;
        }
        return id != null && id.equals(((HighScore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HighScore{" +
            "id=" + getId() +
            ", highestScore=" + getHighestScore() +
            "}";
    }
}
