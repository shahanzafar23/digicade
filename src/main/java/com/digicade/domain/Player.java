package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "game_play_credits")
    private Integer gamePlayCredits;

    @Column(name = "tix")
    private Integer tix;

    @Column(name = "comp")
    private Integer comp;

    @Column(name = "level")
    private Integer level;

    @Column(name = "wallet_address")
    private String walletAddress;

    @JsonIgnoreProperties(value = { "player" }, allowSetters = true)
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private DigiUser digiUser;

    //"game"
    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player" }, allowSetters = true)
    private Set<GameScore> gameScores = new HashSet<>();

    //"game"
    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player" }, allowSetters = true)
    private Set<HighScore> highScores = new HashSet<>();

    //"game"
    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player" }, allowSetters = true)
    private Set<GameBadge> gameBadges = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player", "coinPackage" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player", "couponReward" }, allowSetters = true)
    private Set<PlayerCouponReward> playerCouponRewards = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player", "nftReward" }, allowSetters = true)
    private Set<PlayerNftReward> playerNftRewards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Player id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGamePlayCredits() {
        return this.gamePlayCredits;
    }

    public Player gamePlayCredits(Integer gamePlayCredits) {
        this.setGamePlayCredits(gamePlayCredits);
        return this;
    }

    public void setGamePlayCredits(Integer gamePlayCredits) {
        this.gamePlayCredits = gamePlayCredits;
    }

    public Integer getTix() {
        return this.tix;
    }

    public Player tix(Integer tix) {
        this.setTix(tix);
        return this;
    }

    public void setTix(Integer tix) {
        this.tix = tix;
    }

    public Integer getComp() {
        return this.comp;
    }

    public Player comp(Integer comp) {
        this.setComp(comp);
        return this;
    }

    public void setComp(Integer comp) {
        this.comp = comp;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Player level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getWalletAddress() {
        return this.walletAddress;
    }

    public Player walletAddress(String walletAddress) {
        this.setWalletAddress(walletAddress);
        return this;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public DigiUser getDigiUser() {
        return this.digiUser;
    }

    public void setDigiUser(DigiUser digiUser) {
        this.digiUser = digiUser;
    }

    public Player digiUser(DigiUser digiUser) {
        this.setDigiUser(digiUser);
        return this;
    }

    public Set<GameScore> getGameScores() {
        return this.gameScores;
    }

    public void setGameScores(Set<GameScore> gameScores) {
        if (this.gameScores != null) {
            this.gameScores.forEach(i -> i.setPlayer(null));
        }
        if (gameScores != null) {
            gameScores.forEach(i -> i.setPlayer(this));
        }
        this.gameScores = gameScores;
    }

    public Player gameScores(Set<GameScore> gameScores) {
        this.setGameScores(gameScores);
        return this;
    }

    public Player addGameScore(GameScore gameScore) {
        this.gameScores.add(gameScore);
        gameScore.setPlayer(this);
        return this;
    }

    public Player removeGameScore(GameScore gameScore) {
        this.gameScores.remove(gameScore);
        gameScore.setPlayer(null);
        return this;
    }

    public Set<HighScore> getHighScores() {
        return this.highScores;
    }

    public void setHighScores(Set<HighScore> highScores) {
        if (this.highScores != null) {
            this.highScores.forEach(i -> i.setPlayer(null));
        }
        if (highScores != null) {
            highScores.forEach(i -> i.setPlayer(this));
        }
        this.highScores = highScores;
    }

    public Player highScores(Set<HighScore> highScores) {
        this.setHighScores(highScores);
        return this;
    }

    public Player addHighScore(HighScore highScore) {
        this.highScores.add(highScore);
        highScore.setPlayer(this);
        return this;
    }

    public Player removeHighScore(HighScore highScore) {
        this.highScores.remove(highScore);
        highScore.setPlayer(null);
        return this;
    }

    public Set<GameBadge> getGameBadges() {
        return this.gameBadges;
    }

    public void setGameBadges(Set<GameBadge> gameBadges) {
        if (this.gameBadges != null) {
            this.gameBadges.forEach(i -> i.setPlayer(null));
        }
        if (gameBadges != null) {
            gameBadges.forEach(i -> i.setPlayer(this));
        }
        this.gameBadges = gameBadges;
    }

    public Player gameBadges(Set<GameBadge> gameBadges) {
        this.setGameBadges(gameBadges);
        return this;
    }

    public Player addGameBadge(GameBadge gameBadge) {
        this.gameBadges.add(gameBadge);
        gameBadge.setPlayer(this);
        return this;
    }

    public Player removeGameBadge(GameBadge gameBadge) {
        this.gameBadges.remove(gameBadge);
        gameBadge.setPlayer(null);
        return this;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setPlayer(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setPlayer(this));
        }
        this.transactions = transactions;
    }

    public Player transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Player addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setPlayer(this);
        return this;
    }

    public Player removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setPlayer(null);
        return this;
    }

    public Set<PlayerCouponReward> getPlayerCouponRewards() {
        return this.playerCouponRewards;
    }

    public void setPlayerCouponRewards(Set<PlayerCouponReward> playerCouponRewards) {
        if (this.playerCouponRewards != null) {
            this.playerCouponRewards.forEach(i -> i.setPlayer(null));
        }
        if (playerCouponRewards != null) {
            playerCouponRewards.forEach(i -> i.setPlayer(this));
        }
        this.playerCouponRewards = playerCouponRewards;
    }

    public Player playerCouponRewards(Set<PlayerCouponReward> playerCouponRewards) {
        this.setPlayerCouponRewards(playerCouponRewards);
        return this;
    }

    public Player addPlayerCouponReward(PlayerCouponReward playerCouponReward) {
        this.playerCouponRewards.add(playerCouponReward);
        playerCouponReward.setPlayer(this);
        return this;
    }

    public Player removePlayerCouponReward(PlayerCouponReward playerCouponReward) {
        this.playerCouponRewards.remove(playerCouponReward);
        playerCouponReward.setPlayer(null);
        return this;
    }

    public Set<PlayerNftReward> getPlayerNftRewards() {
        return this.playerNftRewards;
    }

    public void setPlayerNftRewards(Set<PlayerNftReward> playerNftRewards) {
        if (this.playerNftRewards != null) {
            this.playerNftRewards.forEach(i -> i.setPlayer(null));
        }
        if (playerNftRewards != null) {
            playerNftRewards.forEach(i -> i.setPlayer(this));
        }
        this.playerNftRewards = playerNftRewards;
    }

    public Player playerNftRewards(Set<PlayerNftReward> playerNftRewards) {
        this.setPlayerNftRewards(playerNftRewards);
        return this;
    }

    public Player addPlayerNftReward(PlayerNftReward playerNftReward) {
        this.playerNftRewards.add(playerNftReward);
        playerNftReward.setPlayer(this);
        return this;
    }

    public Player removePlayerNftReward(PlayerNftReward playerNftReward) {
        this.playerNftRewards.remove(playerNftReward);
        playerNftReward.setPlayer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", gamePlayCredits=" + getGamePlayCredits() +
            ", tix=" + getTix() +
            ", comp=" + getComp() +
            ", level=" + getLevel() +
            ", walletAddress='" + getWalletAddress() + "'" +
            "}";
    }
}
