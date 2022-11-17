package com.digicade.repository;

import com.digicade.domain.GameScore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameScore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameScoreRepository extends JpaRepository<GameScore, Long> {}
