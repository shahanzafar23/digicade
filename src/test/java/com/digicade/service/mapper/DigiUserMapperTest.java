package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DigiUserMapperTest {

    private DigiUserMapper digiUserMapper;

    @BeforeEach
    public void setUp() {
        digiUserMapper = new DigiUserMapperImpl();
    }
}
