package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A GameBadge.
 */
@Entity
@Table(name = "game_badge")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameBadge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "logo_url")
    private String logoUrl;

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

    public GameBadge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public GameBadge logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameBadge game(Game game) {
        this.setGame(game);
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameBadge player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameBadge)) {
            return false;
        }
        return id != null && id.equals(((GameBadge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameBadge{" +
            "id=" + getId() +
            ", logoUrl='" + getLogoUrl() + "'" +
            "}";
    }
}
