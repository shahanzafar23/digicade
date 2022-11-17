package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameLevel.class);
        GameLevel gameLevel1 = new GameLevel();
        gameLevel1.setId(1L);
        GameLevel gameLevel2 = new GameLevel();
        gameLevel2.setId(gameLevel1.getId());
        assertThat(gameLevel1).isEqualTo(gameLevel2);
        gameLevel2.setId(2L);
        assertThat(gameLevel1).isNotEqualTo(gameLevel2);
        gameLevel1.setId(null);
        assertThat(gameLevel1).isNotEqualTo(gameLevel2);
    }
}
