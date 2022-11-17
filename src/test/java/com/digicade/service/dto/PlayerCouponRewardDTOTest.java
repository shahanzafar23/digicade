package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerCouponRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerCouponRewardDTO.class);
        PlayerCouponRewardDTO playerCouponRewardDTO1 = new PlayerCouponRewardDTO();
        playerCouponRewardDTO1.setId(1L);
        PlayerCouponRewardDTO playerCouponRewardDTO2 = new PlayerCouponRewardDTO();
        assertThat(playerCouponRewardDTO1).isNotEqualTo(playerCouponRewardDTO2);
        playerCouponRewardDTO2.setId(playerCouponRewardDTO1.getId());
        assertThat(playerCouponRewardDTO1).isEqualTo(playerCouponRewardDTO2);
        playerCouponRewardDTO2.setId(2L);
        assertThat(playerCouponRewardDTO1).isNotEqualTo(playerCouponRewardDTO2);
        playerCouponRewardDTO1.setId(null);
        assertThat(playerCouponRewardDTO1).isNotEqualTo(playerCouponRewardDTO2);
    }
}
