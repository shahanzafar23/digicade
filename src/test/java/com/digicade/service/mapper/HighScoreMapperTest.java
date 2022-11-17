package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HighScoreMapperTest {

    private HighScoreMapper highScoreMapper;

    @BeforeEach
    public void setUp() {
        highScoreMapper = new HighScoreMapperImpl();
    }
}
