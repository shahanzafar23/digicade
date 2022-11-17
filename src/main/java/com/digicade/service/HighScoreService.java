package com.digicade.service;

import com.digicade.service.dto.HighScoreDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.HighScore}.
 */
public interface HighScoreService {
    /**
     * Save a highScore.
     *
     * @param highScoreDTO the entity to save.
     * @return the persisted entity.
     */
    HighScoreDTO save(HighScoreDTO highScoreDTO);

    /**
     * Updates a highScore.
     *
     * @param highScoreDTO the entity to update.
     * @return the persisted entity.
     */
    HighScoreDTO update(HighScoreDTO highScoreDTO);

    /**
     * Partially updates a highScore.
     *
     * @param highScoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HighScoreDTO> partialUpdate(HighScoreDTO highScoreDTO);

    /**
     * Get all the highScores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HighScoreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" highScore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HighScoreDTO> findOne(Long id);

    /**
     * Delete the "id" highScore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
