package com.digicade.repository;

import com.digicade.domain.CouponImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CouponImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CouponImageRepository extends JpaRepository<CouponImage, Long> {}
