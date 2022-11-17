package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameLevelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameLevelDTO.class);
        GameLevelDTO gameLevelDTO1 = new GameLevelDTO();
        gameLevelDTO1.setId(1L);
        GameLevelDTO gameLevelDTO2 = new GameLevelDTO();
        assertThat(gameLevelDTO1).isNotEqualTo(gameLevelDTO2);
        gameLevelDTO2.setId(gameLevelDTO1.getId());
        assertThat(gameLevelDTO1).isEqualTo(gameLevelDTO2);
        gameLevelDTO2.setId(2L);
        assertThat(gameLevelDTO1).isNotEqualTo(gameLevelDTO2);
        gameLevelDTO1.setId(null);
        assertThat(gameLevelDTO1).isNotEqualTo(gameLevelDTO2);
    }
}
