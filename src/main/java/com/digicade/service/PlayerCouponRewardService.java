package com.digicade.service;

import com.digicade.service.dto.PlayerCouponRewardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.PlayerCouponReward}.
 */
public interface PlayerCouponRewardService {
    /**
     * Save a playerCouponReward.
     *
     * @param playerCouponRewardDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerCouponRewardDTO save(PlayerCouponRewardDTO playerCouponRewardDTO);

    /**
     * Updates a playerCouponReward.
     *
     * @param playerCouponRewardDTO the entity to update.
     * @return the persisted entity.
     */
    PlayerCouponRewardDTO update(PlayerCouponRewardDTO playerCouponRewardDTO);

    /**
     * Partially updates a playerCouponReward.
     *
     * @param playerCouponRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayerCouponRewardDTO> partialUpdate(PlayerCouponRewardDTO playerCouponRewardDTO);

    /**
     * Get all the playerCouponRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayerCouponRewardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playerCouponReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerCouponRewardDTO> findOne(Long id);

    /**
     * Delete the "id" playerCouponReward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
