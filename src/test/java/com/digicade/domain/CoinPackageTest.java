package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoinPackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoinPackage.class);
        CoinPackage coinPackage1 = new CoinPackage();
        coinPackage1.setId(1L);
        CoinPackage coinPackage2 = new CoinPackage();
        coinPackage2.setId(coinPackage1.getId());
        assertThat(coinPackage1).isEqualTo(coinPackage2);
        coinPackage2.setId(2L);
        assertThat(coinPackage1).isNotEqualTo(coinPackage2);
        coinPackage1.setId(null);
        assertThat(coinPackage1).isNotEqualTo(coinPackage2);
    }
}
