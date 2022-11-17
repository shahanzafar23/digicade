package com.digicade.service.mapper;

import com.digicade.domain.NftReward;
import com.digicade.service.dto.NftRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NftReward} and its DTO {@link NftRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface NftRewardMapper extends EntityMapper<NftRewardDTO, NftReward> {}
