package com.digicade.service.mapper;

import com.digicade.domain.CouponReward;
import com.digicade.domain.Player;
import com.digicade.domain.PlayerCouponReward;
import com.digicade.service.dto.CouponRewardDTO;
import com.digicade.service.dto.PlayerCouponRewardDTO;
import com.digicade.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerCouponReward} and its DTO {@link PlayerCouponRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerCouponRewardMapper extends EntityMapper<PlayerCouponRewardDTO, PlayerCouponReward> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    @Mapping(target = "couponReward", source = "couponReward", qualifiedByName = "couponRewardId")
    PlayerCouponRewardDTO toDto(PlayerCouponReward s);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("couponRewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CouponRewardDTO toDtoCouponRewardId(CouponReward couponReward);
}
