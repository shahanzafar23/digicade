package com.digicade.service.mapper;

import com.digicade.domain.CouponReward;
import com.digicade.domain.DailyReward;
import com.digicade.domain.NftReward;
import com.digicade.service.dto.CouponRewardDTO;
import com.digicade.service.dto.DailyRewardDTO;
import com.digicade.service.dto.NftRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DailyReward} and its DTO {@link DailyRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface DailyRewardMapper extends EntityMapper<DailyRewardDTO, DailyReward> {
    @Mapping(target = "couponReward", source = "couponReward", qualifiedByName = "couponRewardId")
    @Mapping(target = "nftReward", source = "nftReward", qualifiedByName = "nftRewardId")
    DailyRewardDTO toDto(DailyReward s);

    @Named("couponRewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CouponRewardDTO toDtoCouponRewardId(CouponReward couponReward);

    @Named("nftRewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NftRewardDTO toDtoNftRewardId(NftReward nftReward);
}
