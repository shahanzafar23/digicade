package com.digicade.repository;

import com.digicade.domain.HighScore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HighScore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {}
