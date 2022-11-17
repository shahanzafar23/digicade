package com.digicade.service.mapper;

import com.digicade.domain.CouponImage;
import com.digicade.domain.CouponReward;
import com.digicade.service.dto.CouponImageDTO;
import com.digicade.service.dto.CouponRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CouponImage} and its DTO {@link CouponImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface CouponImageMapper extends EntityMapper<CouponImageDTO, CouponImage> {
    @Mapping(target = "couponReward", source = "couponReward", qualifiedByName = "couponRewardId")
    CouponImageDTO toDto(CouponImage s);

    @Named("couponRewardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CouponRewardDTO toDtoCouponRewardId(CouponReward couponReward);
}
