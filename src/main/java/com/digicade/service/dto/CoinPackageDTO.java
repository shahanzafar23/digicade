package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.CoinPackage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoinPackageDTO implements Serializable {

    private Long id;

    private Integer coins;

    private Double cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoinPackageDTO)) {
            return false;
        }

        CoinPackageDTO coinPackageDTO = (CoinPackageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coinPackageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoinPackageDTO{" +
            "id=" + getId() +
            ", coins=" + getCoins() +
            ", cost=" + getCost() +
            "}";
    }
}
