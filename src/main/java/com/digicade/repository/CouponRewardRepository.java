package com.digicade.repository;

import com.digicade.domain.CouponReward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CouponReward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CouponRewardRepository extends JpaRepository<CouponReward, Long> {}
