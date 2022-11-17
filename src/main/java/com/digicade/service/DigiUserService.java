package com.digicade.service;

import com.digicade.service.dto.DigiUserDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.DigiUser}.
 */
public interface DigiUserService {
    /**
     * Save a digiUser.
     *
     * @param digiUserDTO the entity to save.
     * @return the persisted entity.
     */
    DigiUserDTO save(DigiUserDTO digiUserDTO);

    /**
     * Updates a digiUser.
     *
     * @param digiUserDTO the entity to update.
     * @return the persisted entity.
     */
    DigiUserDTO update(DigiUserDTO digiUserDTO);

    /**
     * Partially updates a digiUser.
     *
     * @param digiUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DigiUserDTO> partialUpdate(DigiUserDTO digiUserDTO);

    /**
     * Get all the digiUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DigiUserDTO> findAll(Pageable pageable);
    /**
     * Get all the DigiUserDTO where Player is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DigiUserDTO> findAllWherePlayerIsNull();

    /**
     * Get the "id" digiUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DigiUserDTO> findOne(Long id);

    /**
     * Delete the "id" digiUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
