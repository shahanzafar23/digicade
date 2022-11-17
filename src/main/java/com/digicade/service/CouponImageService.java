package com.digicade.service;

import com.digicade.service.dto.CouponImageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.digicade.domain.CouponImage}.
 */
public interface CouponImageService {
    /**
     * Save a couponImage.
     *
     * @param couponImageDTO the entity to save.
     * @return the persisted entity.
     */
    CouponImageDTO save(CouponImageDTO couponImageDTO);

    /**
     * Updates a couponImage.
     *
     * @param couponImageDTO the entity to update.
     * @return the persisted entity.
     */
    CouponImageDTO update(CouponImageDTO couponImageDTO);

    /**
     * Partially updates a couponImage.
     *
     * @param couponImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CouponImageDTO> partialUpdate(CouponImageDTO couponImageDTO);

    /**
     * Get all the couponImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CouponImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" couponImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CouponImageDTO> findOne(Long id);

    /**
     * Delete the "id" couponImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
