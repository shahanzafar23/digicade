package com.digicade.service.mapper;

import com.digicade.domain.DigiUser;
import com.digicade.domain.Player;
import com.digicade.service.dto.DigiUserDTO;
import com.digicade.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {
    @Mapping(target = "digiUser", source = "digiUser", qualifiedByName = "digiUserId")
    PlayerDTO toDto(Player s);

    @Named("digiUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DigiUserDTO toDtoDigiUserId(DigiUser digiUser);
}
