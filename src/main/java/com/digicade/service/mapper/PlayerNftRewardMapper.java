package com.digicade.service.mapper;

import com.digicade.domain.NftReward;
import com.digicade.domain.Player;
import com.digicade.domain.PlayerNftReward;
import com.digicade.service.dto.NftRewardDTO;
import com.digicade.service.dto.PlayerDTO;
import com.digicade.service.dto.PlayerNftRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerNftReward} and its DTO {@link PlayerNftRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerNftRewardMapper extends EntityMapper<PlayerNftRewardDTO, PlayerNftReward> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    @Mapping(target = "nftReward", source = "nftReward", qualifiedByName = "nftRewardId")
    PlayerNftRewardDTO toDto(PlayerNftReward s);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("nftRewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NftRewardDTO toDtoNftRewardId(NftReward nftReward);
}
