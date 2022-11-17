package com.digicade.repository;

import com.digicade.domain.NftReward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NftReward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NftRewardRepository extends JpaRepository<NftReward, Long> {}
