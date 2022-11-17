package com.digicade.repository;

import com.digicade.domain.GameLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameLevelRepository extends JpaRepository<GameLevel, Long> {}
