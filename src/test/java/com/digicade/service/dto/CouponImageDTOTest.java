package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouponImageDTO.class);
        CouponImageDTO couponImageDTO1 = new CouponImageDTO();
        couponImageDTO1.setId(1L);
        CouponImageDTO couponImageDTO2 = new CouponImageDTO();
        assertThat(couponImageDTO1).isNotEqualTo(couponImageDTO2);
        couponImageDTO2.setId(couponImageDTO1.getId());
        assertThat(couponImageDTO1).isEqualTo(couponImageDTO2);
        couponImageDTO2.setId(2L);
        assertThat(couponImageDTO1).isNotEqualTo(couponImageDTO2);
        couponImageDTO1.setId(null);
        assertThat(couponImageDTO1).isNotEqualTo(couponImageDTO2);
    }
}
