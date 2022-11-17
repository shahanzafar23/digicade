package com.digicade.service;

import com.digicade.service.dto.PlayerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.Player}.
 */
public interface PlayerService {
    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerDTO save(PlayerDTO playerDTO);

    /**
     * Updates a player.
     *
     * @param playerDTO the entity to update.
     * @return the persisted entity.
     */
    PlayerDTO update(PlayerDTO playerDTO);

    /**
     * Partially updates a player.
     *
     * @param playerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayerDTO> partialUpdate(PlayerDTO playerDTO);

    /**
     * Get all the players.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" player.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerDTO> findOne(Long id);

    /**
     * Delete the "id" player.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
