package com.digicade.service;

import com.digicade.service.dto.DailyRewardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.DailyReward}.
 */
public interface DailyRewardService {
    /**
     * Save a dailyReward.
     *
     * @param dailyRewardDTO the entity to save.
     * @return the persisted entity.
     */
    DailyRewardDTO save(DailyRewardDTO dailyRewardDTO);

    /**
     * Updates a dailyReward.
     *
     * @param dailyRewardDTO the entity to update.
     * @return the persisted entity.
     */
    DailyRewardDTO update(DailyRewardDTO dailyRewardDTO);

    /**
     * Partially updates a dailyReward.
     *
     * @param dailyRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DailyRewardDTO> partialUpdate(DailyRewardDTO dailyRewardDTO);

    /**
     * Get all the dailyRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DailyRewardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dailyReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DailyRewardDTO> findOne(Long id);

    /**
     * Delete the "id" dailyReward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
