package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameScoreMapperTest {

    private GameScoreMapper gameScoreMapper;

    @BeforeEach
    public void setUp() {
        gameScoreMapper = new GameScoreMapperImpl();
    }
}
