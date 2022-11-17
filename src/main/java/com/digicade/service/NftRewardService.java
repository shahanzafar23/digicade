package com.digicade.service;

import com.digicade.service.dto.NftRewardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.NftReward}.
 */
public interface NftRewardService {
    /**
     * Save a nftReward.
     *
     * @param nftRewardDTO the entity to save.
     * @return the persisted entity.
     */
    NftRewardDTO save(NftRewardDTO nftRewardDTO);

    /**
     * Updates a nftReward.
     *
     * @param nftRewardDTO the entity to update.
     * @return the persisted entity.
     */
    NftRewardDTO update(NftRewardDTO nftRewardDTO);

    /**
     * Partially updates a nftReward.
     *
     * @param nftRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NftRewardDTO> partialUpdate(NftRewardDTO nftRewardDTO);

    /**
     * Get all the nftRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NftRewardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" nftReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NftRewardDTO> findOne(Long id);

    /**
     * Delete the "id" nftReward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
