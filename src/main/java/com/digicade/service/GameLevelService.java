package com.digicade.service;

import com.digicade.service.dto.GameLevelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.GameLevel}.
 */
public interface GameLevelService {
    /**
     * Save a gameLevel.
     *
     * @param gameLevelDTO the entity to save.
     * @return the persisted entity.
     */
    GameLevelDTO save(GameLevelDTO gameLevelDTO);

    /**
     * Updates a gameLevel.
     *
     * @param gameLevelDTO the entity to update.
     * @return the persisted entity.
     */
    GameLevelDTO update(GameLevelDTO gameLevelDTO);

    /**
     * Partially updates a gameLevel.
     *
     * @param gameLevelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameLevelDTO> partialUpdate(GameLevelDTO gameLevelDTO);

    /**
     * Get all the gameLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GameLevelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" gameLevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameLevelDTO> findOne(Long id);

    /**
     * Delete the "id" gameLevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
