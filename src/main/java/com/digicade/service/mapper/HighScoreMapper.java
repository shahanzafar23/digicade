package com.digicade.service.mapper;

import com.digicade.domain.Game;
import com.digicade.domain.HighScore;
import com.digicade.domain.Player;
import com.digicade.service.dto.GameDTO;
import com.digicade.service.dto.HighScoreDTO;
import com.digicade.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HighScore} and its DTO {@link HighScoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface HighScoreMapper extends EntityMapper<HighScoreDTO, HighScore> {
    @Mapping(target = "game", source = "game", qualifiedByName = "gameId")
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    HighScoreDTO toDto(HighScore s);

    @Named("gameId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameDTO toDtoGameId(Game game);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);
}
