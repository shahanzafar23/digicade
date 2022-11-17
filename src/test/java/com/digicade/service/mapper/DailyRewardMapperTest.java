package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DailyRewardMapperTest {

    private DailyRewardMapper dailyRewardMapper;

    @BeforeEach
    public void setUp() {
        dailyRewardMapper = new DailyRewardMapperImpl();
    }
}
