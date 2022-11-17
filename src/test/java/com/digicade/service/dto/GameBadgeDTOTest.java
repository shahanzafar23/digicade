package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameBadgeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameBadgeDTO.class);
        GameBadgeDTO gameBadgeDTO1 = new GameBadgeDTO();
        gameBadgeDTO1.setId(1L);
        GameBadgeDTO gameBadgeDTO2 = new GameBadgeDTO();
        assertThat(gameBadgeDTO1).isNotEqualTo(gameBadgeDTO2);
        gameBadgeDTO2.setId(gameBadgeDTO1.getId());
        assertThat(gameBadgeDTO1).isEqualTo(gameBadgeDTO2);
        gameBadgeDTO2.setId(2L);
        assertThat(gameBadgeDTO1).isNotEqualTo(gameBadgeDTO2);
        gameBadgeDTO1.setId(null);
        assertThat(gameBadgeDTO1).isNotEqualTo(gameBadgeDTO2);
    }
}
