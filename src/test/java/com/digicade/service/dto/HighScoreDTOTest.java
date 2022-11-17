package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HighScoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HighScoreDTO.class);
        HighScoreDTO highScoreDTO1 = new HighScoreDTO();
        highScoreDTO1.setId(1L);
        HighScoreDTO highScoreDTO2 = new HighScoreDTO();
        assertThat(highScoreDTO1).isNotEqualTo(highScoreDTO2);
        highScoreDTO2.setId(highScoreDTO1.getId());
        assertThat(highScoreDTO1).isEqualTo(highScoreDTO2);
        highScoreDTO2.setId(2L);
        assertThat(highScoreDTO1).isNotEqualTo(highScoreDTO2);
        highScoreDTO1.setId(null);
        assertThat(highScoreDTO1).isNotEqualTo(highScoreDTO2);
    }
}
