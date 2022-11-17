package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerNftRewardMapperTest {

    private PlayerNftRewardMapper playerNftRewardMapper;

    @BeforeEach
    public void setUp() {
        playerNftRewardMapper = new PlayerNftRewardMapperImpl();
    }
}
