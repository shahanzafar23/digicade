package com.digicade.service.mapper;

import com.digicade.domain.CouponReward;
import com.digicade.service.dto.CouponRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CouponReward} and its DTO {@link CouponRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CouponRewardMapper extends EntityMapper<CouponRewardDTO, CouponReward> {}
