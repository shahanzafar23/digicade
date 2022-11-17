package com.digicade.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.digicade.domain.NftReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NftRewardDTO implements Serializable {

    private Long id;

    private Integer tix;

    private Integer comp;

    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTix() {
        return tix;
    }

    public void setTix(Integer tix) {
        this.tix = tix;
    }

    public Integer getComp() {
        return comp;
    }

    public void setComp(Integer comp) {
        this.comp = comp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NftRewardDTO)) {
            return false;
        }

        NftRewardDTO nftRewardDTO = (NftRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nftRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NftRewardDTO{" +
            "id=" + getId() +
            ", tix=" + getTix() +
            ", comp=" + getComp() +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
