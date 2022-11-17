package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameLevelMapperTest {

    private GameLevelMapper gameLevelMapper;

    @BeforeEach
    public void setUp() {
        gameLevelMapper = new GameLevelMapperImpl();
    }
}
