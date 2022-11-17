package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NftRewardMapperTest {

    private NftRewardMapper nftRewardMapper;

    @BeforeEach
    public void setUp() {
        nftRewardMapper = new NftRewardMapperImpl();
    }
}
