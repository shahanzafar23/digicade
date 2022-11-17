package com.digicade.service.mapper;

import com.digicade.domain.DigiUser;
import com.digicade.service.dto.DigiUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DigiUser} and its DTO {@link DigiUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface DigiUserMapper extends EntityMapper<DigiUserDTO, DigiUser> {}
