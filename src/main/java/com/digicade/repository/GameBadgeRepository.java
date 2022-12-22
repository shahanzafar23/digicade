package com.digicade.repository;

import com.digicade.domain.GameBadge;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameBadge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameBadgeRepository extends JpaRepository<GameBadge, Long> {
    Set<GameBadge> findGameBadgeByPlayerId(Long id);
}
