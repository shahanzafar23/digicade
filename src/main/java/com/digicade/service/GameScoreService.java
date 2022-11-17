package com.digicade.service;

import com.digicade.service.dto.GameScoreDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.GameScore}.
 */
public interface GameScoreService {
    /**
     * Save a gameScore.
     *
     * @param gameScoreDTO the entity to save.
     * @return the persisted entity.
     */
    GameScoreDTO save(GameScoreDTO gameScoreDTO);

    /**
     * Updates a gameScore.
     *
     * @param gameScoreDTO the entity to update.
     * @return the persisted entity.
     */
    GameScoreDTO update(GameScoreDTO gameScoreDTO);

    /**
     * Partially updates a gameScore.
     *
     * @param gameScoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameScoreDTO> partialUpdate(GameScoreDTO gameScoreDTO);

    /**
     * Get all the gameScores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GameScoreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" gameScore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameScoreDTO> findOne(Long id);

    /**
     * Delete the "id" gameScore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
