package com.digicade.service;

import com.digicade.service.dto.PlayerNftRewardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.PlayerNftReward}.
 */
public interface PlayerNftRewardService {
    /**
     * Save a playerNftReward.
     *
     * @param playerNftRewardDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerNftRewardDTO save(PlayerNftRewardDTO playerNftRewardDTO);

    /**
     * Updates a playerNftReward.
     *
     * @param playerNftRewardDTO the entity to update.
     * @return the persisted entity.
     */
    PlayerNftRewardDTO update(PlayerNftRewardDTO playerNftRewardDTO);

    /**
     * Partially updates a playerNftReward.
     *
     * @param playerNftRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayerNftRewardDTO> partialUpdate(PlayerNftRewardDTO playerNftRewardDTO);

    /**
     * Get all the playerNftRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayerNftRewardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playerNftReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerNftRewardDTO> findOne(Long id);

    /**
     * Delete the "id" playerNftReward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
