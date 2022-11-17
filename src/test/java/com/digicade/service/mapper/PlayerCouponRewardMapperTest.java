package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerCouponRewardMapperTest {

    private PlayerCouponRewardMapper playerCouponRewardMapper;

    @BeforeEach
    public void setUp() {
        playerCouponRewardMapper = new PlayerCouponRewardMapperImpl();
    }
}
