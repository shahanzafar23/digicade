package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DailyRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyRewardDTO.class);
        DailyRewardDTO dailyRewardDTO1 = new DailyRewardDTO();
        dailyRewardDTO1.setId(1L);
        DailyRewardDTO dailyRewardDTO2 = new DailyRewardDTO();
        assertThat(dailyRewardDTO1).isNotEqualTo(dailyRewardDTO2);
        dailyRewardDTO2.setId(dailyRewardDTO1.getId());
        assertThat(dailyRewardDTO1).isEqualTo(dailyRewardDTO2);
        dailyRewardDTO2.setId(2L);
        assertThat(dailyRewardDTO1).isNotEqualTo(dailyRewardDTO2);
        dailyRewardDTO1.setId(null);
        assertThat(dailyRewardDTO1).isNotEqualTo(dailyRewardDTO2);
    }
}
