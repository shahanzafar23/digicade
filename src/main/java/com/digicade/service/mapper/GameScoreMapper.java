package com.digicade.service.mapper;

import com.digicade.domain.Game;
import com.digicade.domain.GameScore;
import com.digicade.domain.Player;
import com.digicade.service.dto.GameDTO;
import com.digicade.service.dto.GameScoreDTO;
import com.digicade.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GameScore} and its DTO {@link GameScoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameScoreMapper extends EntityMapper<GameScoreDTO, GameScore> {
    @Mapping(target = "game", source = "game", qualifiedByName = "gameId")
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    GameScoreDTO toDto(GameScore s);

    @Named("gameId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameDTO toDtoGameId(Game game);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);
}
