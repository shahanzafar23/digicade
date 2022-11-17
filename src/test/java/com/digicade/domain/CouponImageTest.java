package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouponImage.class);
        CouponImage couponImage1 = new CouponImage();
        couponImage1.setId(1L);
        CouponImage couponImage2 = new CouponImage();
        couponImage2.setId(couponImage1.getId());
        assertThat(couponImage1).isEqualTo(couponImage2);
        couponImage2.setId(2L);
        assertThat(couponImage1).isNotEqualTo(couponImage2);
        couponImage1.setId(null);
        assertThat(couponImage1).isNotEqualTo(couponImage2);
    }
}
