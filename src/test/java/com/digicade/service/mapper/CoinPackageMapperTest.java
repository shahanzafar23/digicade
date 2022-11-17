package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoinPackageMapperTest {

    private CoinPackageMapper coinPackageMapper;

    @BeforeEach
    public void setUp() {
        coinPackageMapper = new CoinPackageMapperImpl();
    }
}
