package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerCouponRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerCouponReward.class);
        PlayerCouponReward playerCouponReward1 = new PlayerCouponReward();
        playerCouponReward1.setId(1L);
        PlayerCouponReward playerCouponReward2 = new PlayerCouponReward();
        playerCouponReward2.setId(playerCouponReward1.getId());
        assertThat(playerCouponReward1).isEqualTo(playerCouponReward2);
        playerCouponReward2.setId(2L);
        assertThat(playerCouponReward1).isNotEqualTo(playerCouponReward2);
        playerCouponReward1.setId(null);
        assertThat(playerCouponReward1).isNotEqualTo(playerCouponReward2);
    }
}
