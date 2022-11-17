package com.digicade.service.mapper;

import com.digicade.domain.Game;
import com.digicade.service.dto.GameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameMapper extends EntityMapper<GameDTO, Game> {}
