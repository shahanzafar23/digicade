package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouponRewardDTO.class);
        CouponRewardDTO couponRewardDTO1 = new CouponRewardDTO();
        couponRewardDTO1.setId(1L);
        CouponRewardDTO couponRewardDTO2 = new CouponRewardDTO();
        assertThat(couponRewardDTO1).isNotEqualTo(couponRewardDTO2);
        couponRewardDTO2.setId(couponRewardDTO1.getId());
        assertThat(couponRewardDTO1).isEqualTo(couponRewardDTO2);
        couponRewardDTO2.setId(2L);
        assertThat(couponRewardDTO1).isNotEqualTo(couponRewardDTO2);
        couponRewardDTO1.setId(null);
        assertThat(couponRewardDTO1).isNotEqualTo(couponRewardDTO2);
    }
}
