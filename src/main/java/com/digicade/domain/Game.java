package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "logo_url")
    private String logoUrl;

    //"game"
    @OneToMany(mappedBy = "game")
    @JsonIgnoreProperties(value = { "player" }, allowSetters = true)
    private Set<GameScore> gameScores = new HashSet<>();

    @OneToMany(mappedBy = "game")
    @JsonIgnoreProperties(value = { "game", "player" }, allowSetters = true)
    private Set<HighScore> highScores = new HashSet<>();

    @OneToMany(mappedBy = "game")
    @JsonIgnoreProperties(value = { "game", "player" }, allowSetters = true)
    private Set<GameBadge> gameBadges = new HashSet<>();

    @OneToMany(mappedBy = "game")
    @JsonIgnoreProperties(value = { "game" }, allowSetters = true)
    private Set<GameLevel> gameLevels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Game id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public Game url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public Game logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Set<GameScore> getGameScores() {
        return this.gameScores;
    }

    public void setGameScores(Set<GameScore> gameScores) {
        if (this.gameScores != null) {
            this.gameScores.forEach(i -> i.setGame(null));
        }
        if (gameScores != null) {
            gameScores.forEach(i -> i.setGame(this));
        }
        this.gameScores = gameScores;
    }

    public Game gameScores(Set<GameScore> gameScores) {
        this.setGameScores(gameScores);
        return this;
    }

    public Game addGameScore(GameScore gameScore) {
        this.gameScores.add(gameScore);
        gameScore.setGame(this);
        return this;
    }

    public Game removeGameScore(GameScore gameScore) {
        this.gameScores.remove(gameScore);
        gameScore.setGame(null);
        return this;
    }

    public Set<HighScore> getHighScores() {
        return this.highScores;
    }

    public void setHighScores(Set<HighScore> highScores) {
        if (this.highScores != null) {
            this.highScores.forEach(i -> i.setGame(null));
        }
        if (highScores != null) {
            highScores.forEach(i -> i.setGame(this));
        }
        this.highScores = highScores;
    }

    public Game highScores(Set<HighScore> highScores) {
        this.setHighScores(highScores);
        return this;
    }

    public Game addHighScore(HighScore highScore) {
        this.highScores.add(highScore);
        highScore.setGame(this);
        return this;
    }

    public Game removeHighScore(HighScore highScore) {
        this.highScores.remove(highScore);
        highScore.setGame(null);
        return this;
    }

    public Set<GameBadge> getGameBadges() {
        return this.gameBadges;
    }

    public void setGameBadges(Set<GameBadge> gameBadges) {
        if (this.gameBadges != null) {
            this.gameBadges.forEach(i -> i.setGame(null));
        }
        if (gameBadges != null) {
            gameBadges.forEach(i -> i.setGame(this));
        }
        this.gameBadges = gameBadges;
    }

    public Game gameBadges(Set<GameBadge> gameBadges) {
        this.setGameBadges(gameBadges);
        return this;
    }

    public Game addGameBadge(GameBadge gameBadge) {
        this.gameBadges.add(gameBadge);
        gameBadge.setGame(this);
        return this;
    }

    public Game removeGameBadge(GameBadge gameBadge) {
        this.gameBadges.remove(gameBadge);
        gameBadge.setGame(null);
        return this;
    }

    public Set<GameLevel> getGameLevels() {
        return this.gameLevels;
    }

    public void setGameLevels(Set<GameLevel> gameLevels) {
        if (this.gameLevels != null) {
            this.gameLevels.forEach(i -> i.setGame(null));
        }
        if (gameLevels != null) {
            gameLevels.forEach(i -> i.setGame(this));
        }
        this.gameLevels = gameLevels;
    }

    public Game gameLevels(Set<GameLevel> gameLevels) {
        this.setGameLevels(gameLevels);
        return this;
    }

    public Game addGameLevel(GameLevel gameLevel) {
        this.gameLevels.add(gameLevel);
        gameLevel.setGame(this);
        return this;
    }

    public Game removeGameLevel(GameLevel gameLevel) {
        this.gameLevels.remove(gameLevel);
        gameLevel.setGame(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            "}";
    }
}
