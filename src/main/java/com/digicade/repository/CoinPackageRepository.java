package com.digicade.repository;

import com.digicade.domain.CoinPackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CoinPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoinPackageRepository extends JpaRepository<CoinPackage, Long> {}
