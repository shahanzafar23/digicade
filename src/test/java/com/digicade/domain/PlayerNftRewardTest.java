package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerNftRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerNftReward.class);
        PlayerNftReward playerNftReward1 = new PlayerNftReward();
        playerNftReward1.setId(1L);
        PlayerNftReward playerNftReward2 = new PlayerNftReward();
        playerNftReward2.setId(playerNftReward1.getId());
        assertThat(playerNftReward1).isEqualTo(playerNftReward2);
        playerNftReward2.setId(2L);
        assertThat(playerNftReward1).isNotEqualTo(playerNftReward2);
        playerNftReward1.setId(null);
        assertThat(playerNftReward1).isNotEqualTo(playerNftReward2);
    }
}
