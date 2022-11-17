package com.digicade.service;

import com.digicade.service.dto.GameBadgeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.GameBadge}.
 */
public interface GameBadgeService {
    /**
     * Save a gameBadge.
     *
     * @param gameBadgeDTO the entity to save.
     * @return the persisted entity.
     */
    GameBadgeDTO save(GameBadgeDTO gameBadgeDTO);

    /**
     * Updates a gameBadge.
     *
     * @param gameBadgeDTO the entity to update.
     * @return the persisted entity.
     */
    GameBadgeDTO update(GameBadgeDTO gameBadgeDTO);

    /**
     * Partially updates a gameBadge.
     *
     * @param gameBadgeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameBadgeDTO> partialUpdate(GameBadgeDTO gameBadgeDTO);

    /**
     * Get all the gameBadges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GameBadgeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" gameBadge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameBadgeDTO> findOne(Long id);

    /**
     * Delete the "id" gameBadge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
