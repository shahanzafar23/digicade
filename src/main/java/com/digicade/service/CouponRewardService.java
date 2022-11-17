package com.digicade.service;

import com.digicade.service.dto.CouponRewardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.CouponReward}.
 */
public interface CouponRewardService {
    /**
     * Save a couponReward.
     *
     * @param couponRewardDTO the entity to save.
     * @return the persisted entity.
     */
    CouponRewardDTO save(CouponRewardDTO couponRewardDTO);

    /**
     * Updates a couponReward.
     *
     * @param couponRewardDTO the entity to update.
     * @return the persisted entity.
     */
    CouponRewardDTO update(CouponRewardDTO couponRewardDTO);

    /**
     * Partially updates a couponReward.
     *
     * @param couponRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CouponRewardDTO> partialUpdate(CouponRewardDTO couponRewardDTO);

    /**
     * Get all the couponRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CouponRewardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" couponReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CouponRewardDTO> findOne(Long id);

    /**
     * Delete the "id" couponReward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
