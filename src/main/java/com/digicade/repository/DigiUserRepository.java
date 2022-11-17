package com.digicade.repository;

import com.digicade.domain.DigiUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DigiUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DigiUserRepository extends JpaRepository<DigiUser, Long> {}
