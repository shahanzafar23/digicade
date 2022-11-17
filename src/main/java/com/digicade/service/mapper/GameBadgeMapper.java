package com.digicade.service.mapper;

import com.digicade.domain.Game;
import com.digicade.domain.GameBadge;
import com.digicade.domain.Player;
import com.digicade.service.dto.GameBadgeDTO;
import com.digicade.service.dto.GameDTO;
import com.digicade.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GameBadge} and its DTO {@link GameBadgeDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameBadgeMapper extends EntityMapper<GameBadgeDTO, GameBadge> {
    @Mapping(target = "game", source = "game", qualifiedByName = "gameId")
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    GameBadgeDTO toDto(GameBadge s);

    @Named("gameId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameDTO toDtoGameId(Game game);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);
}
