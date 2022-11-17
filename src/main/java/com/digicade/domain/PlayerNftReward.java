package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A PlayerNftReward.
 */
@Entity
@Table(name = "player_nft_reward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerNftReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "digiUser", "gameScores", "highScores", "gameBadges", "transactions", "playerCouponRewards", "playerNftRewards" },
        allowSetters = true
    )
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties(value = { "playerNftRewards", "dailyRewards" }, allowSetters = true)
    private NftReward nftReward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerNftReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PlayerNftReward date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerNftReward player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public NftReward getNftReward() {
        return this.nftReward;
    }

    public void setNftReward(NftReward nftReward) {
        this.nftReward = nftReward;
    }

    public PlayerNftReward nftReward(NftReward nftReward) {
        this.setNftReward(nftReward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerNftReward)) {
            return false;
        }
        return id != null && id.equals(((PlayerNftReward) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerNftReward{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
