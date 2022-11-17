package com.digicade.repository;

import com.digicade.domain.DailyReward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DailyReward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyRewardRepository extends JpaRepository<DailyReward, Long> {}
