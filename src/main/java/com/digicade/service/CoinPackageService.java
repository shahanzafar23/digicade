package com.digicade.service;

import com.digicade.service.dto.CoinPackageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.CoinPackage}.
 */
public interface CoinPackageService {
    /**
     * Save a coinPackage.
     *
     * @param coinPackageDTO the entity to save.
     * @return the persisted entity.
     */
    CoinPackageDTO save(CoinPackageDTO coinPackageDTO);

    /**
     * Updates a coinPackage.
     *
     * @param coinPackageDTO the entity to update.
     * @return the persisted entity.
     */
    CoinPackageDTO update(CoinPackageDTO coinPackageDTO);

    /**
     * Partially updates a coinPackage.
     *
     * @param coinPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoinPackageDTO> partialUpdate(CoinPackageDTO coinPackageDTO);

    /**
     * Get all the coinPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoinPackageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" coinPackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoinPackageDTO> findOne(Long id);

    /**
     * Delete the "id" coinPackage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
