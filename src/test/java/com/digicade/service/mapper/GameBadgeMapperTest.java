package com.digicade.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBadgeMapperTest {

    private GameBadgeMapper gameBadgeMapper;

    @BeforeEach
    public void setUp() {
        gameBadgeMapper = new GameBadgeMapperImpl();
    }
}
