package com.digicade.service.mapper;

import com.digicade.domain.CoinPackage;
import com.digicade.service.dto.CoinPackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoinPackage} and its DTO {@link CoinPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoinPackageMapper extends EntityMapper<CoinPackageDTO, CoinPackage> {}
