package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouponReward.class);
        CouponReward couponReward1 = new CouponReward();
        couponReward1.setId(1L);
        CouponReward couponReward2 = new CouponReward();
        couponReward2.setId(couponReward1.getId());
        assertThat(couponReward1).isEqualTo(couponReward2);
        couponReward2.setId(2L);
        assertThat(couponReward1).isNotEqualTo(couponReward2);
        couponReward1.setId(null);
        assertThat(couponReward1).isNotEqualTo(couponReward2);
    }
}
