package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CouponRewardMapperTest {

    private CouponRewardMapper couponRewardMapper;

    @BeforeEach
    public void setUp() {
        couponRewardMapper = new CouponRewardMapperImpl();
    }
}
