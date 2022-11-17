package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameBadgeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameBadge.class);
        GameBadge gameBadge1 = new GameBadge();
        gameBadge1.setId(1L);
        GameBadge gameBadge2 = new GameBadge();
        gameBadge2.setId(gameBadge1.getId());
        assertThat(gameBadge1).isEqualTo(gameBadge2);
        gameBadge2.setId(2L);
        assertThat(gameBadge1).isNotEqualTo(gameBadge2);
        gameBadge1.setId(null);
        assertThat(gameBadge1).isNotEqualTo(gameBadge2);
    }
}
