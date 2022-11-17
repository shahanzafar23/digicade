package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DailyRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyReward.class);
        DailyReward dailyReward1 = new DailyReward();
        dailyReward1.setId(1L);
        DailyReward dailyReward2 = new DailyReward();
        dailyReward2.setId(dailyReward1.getId());
        assertThat(dailyReward1).isEqualTo(dailyReward2);
        dailyReward2.setId(2L);
        assertThat(dailyReward1).isNotEqualTo(dailyReward2);
        dailyReward1.setId(null);
        assertThat(dailyReward1).isNotEqualTo(dailyReward2);
    }
}
