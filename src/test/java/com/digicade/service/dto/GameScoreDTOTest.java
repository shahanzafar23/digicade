package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameScoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameScoreDTO.class);
        GameScoreDTO gameScoreDTO1 = new GameScoreDTO();
        gameScoreDTO1.setId(1L);
        GameScoreDTO gameScoreDTO2 = new GameScoreDTO();
        assertThat(gameScoreDTO1).isNotEqualTo(gameScoreDTO2);
        gameScoreDTO2.setId(gameScoreDTO1.getId());
        assertThat(gameScoreDTO1).isEqualTo(gameScoreDTO2);
        gameScoreDTO2.setId(2L);
        assertThat(gameScoreDTO1).isNotEqualTo(gameScoreDTO2);
        gameScoreDTO1.setId(null);
        assertThat(gameScoreDTO1).isNotEqualTo(gameScoreDTO2);
    }
}
