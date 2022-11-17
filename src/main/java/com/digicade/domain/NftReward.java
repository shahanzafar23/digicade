package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A NftReward.
 */
@Entity
@Table(name = "nft_reward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NftReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tix")
    private Integer tix;

    @Column(name = "comp")
    private Integer comp;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "nftReward")
    @JsonIgnoreProperties(value = { "player", "nftReward" }, allowSetters = true)
    private Set<PlayerNftReward> playerNftRewards = new HashSet<>();

    @OneToMany(mappedBy = "nftReward")
    @JsonIgnoreProperties(value = { "couponReward", "nftReward" }, allowSetters = true)
    private Set<DailyReward> dailyRewards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NftReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTix() {
        return this.tix;
    }

    public NftReward tix(Integer tix) {
        this.setTix(tix);
        return this;
    }

    public void setTix(Integer tix) {
        this.tix = tix;
    }

    public Integer getComp() {
        return this.comp;
    }

    public NftReward comp(Integer comp) {
        this.setComp(comp);
        return this;
    }

    public void setComp(Integer comp) {
        this.comp = comp;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public NftReward imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<PlayerNftReward> getPlayerNftRewards() {
        return this.playerNftRewards;
    }

    public void setPlayerNftRewards(Set<PlayerNftReward> playerNftRewards) {
        if (this.playerNftRewards != null) {
            this.playerNftRewards.forEach(i -> i.setNftReward(null));
        }
        if (playerNftRewards != null) {
            playerNftRewards.forEach(i -> i.setNftReward(this));
        }
        this.playerNftRewards = playerNftRewards;
    }

    public NftReward playerNftRewards(Set<PlayerNftReward> playerNftRewards) {
        this.setPlayerNftRewards(playerNftRewards);
        return this;
    }

    public NftReward addPlayerNftReward(PlayerNftReward playerNftReward) {
        this.playerNftRewards.add(playerNftReward);
        playerNftReward.setNftReward(this);
        return this;
    }

    public NftReward removePlayerNftReward(PlayerNftReward playerNftReward) {
        this.playerNftRewards.remove(playerNftReward);
        playerNftReward.setNftReward(null);
        return this;
    }

    public Set<DailyReward> getDailyRewards() {
        return this.dailyRewards;
    }

    public void setDailyRewards(Set<DailyReward> dailyRewards) {
        if (this.dailyRewards != null) {
            this.dailyRewards.forEach(i -> i.setNftReward(null));
        }
        if (dailyRewards != null) {
            dailyRewards.forEach(i -> i.setNftReward(this));
        }
        this.dailyRewards = dailyRewards;
    }

    public NftReward dailyRewards(Set<DailyReward> dailyRewards) {
        this.setDailyRewards(dailyRewards);
        return this;
    }

    public NftReward addDailyReward(DailyReward dailyReward) {
        this.dailyRewards.add(dailyReward);
        dailyReward.setNftReward(this);
        return this;
    }

    public NftReward removeDailyReward(DailyReward dailyReward) {
        this.dailyRewards.remove(dailyReward);
        dailyReward.setNftReward(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NftReward)) {
            return false;
        }
        return id != null && id.equals(((NftReward) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NftReward{" +
            "id=" + getId() +
            ", tix=" + getTix() +
            ", comp=" + getComp() +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
