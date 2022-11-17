package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CouponImageMapperTest {

    private CouponImageMapper couponImageMapper;

    @BeforeEach
    public void setUp() {
        couponImageMapper = new CouponImageMapperImpl();
    }
}
