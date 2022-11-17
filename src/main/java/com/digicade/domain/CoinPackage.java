package com.digicade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A CoinPackage.
 */
@Entity
@Table(name = "coin_package")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoinPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "coins")
    private Integer coins;

    @Column(name = "cost")
    private Double cost;

    @OneToMany(mappedBy = "coinPackage")
    @JsonIgnoreProperties(value = { "player", "coinPackage" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CoinPackage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCoins() {
        return this.coins;
    }

    public CoinPackage coins(Integer coins) {
        this.setCoins(coins);
        return this;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Double getCost() {
        return this.cost;
    }

    public CoinPackage cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setCoinPackage(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setCoinPackage(this));
        }
        this.transactions = transactions;
    }

    public CoinPackage transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public CoinPackage addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCoinPackage(this);
        return this;
    }

    public CoinPackage removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setCoinPackage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoinPackage)) {
            return false;
        }
        return id != null && id.equals(((CoinPackage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoinPackage{" +
            "id=" + getId() +
            ", coins=" + getCoins() +
            ", cost=" + getCost() +
            "}";
    }
}
