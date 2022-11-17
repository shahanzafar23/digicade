package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoinPackageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoinPackageDTO.class);
        CoinPackageDTO coinPackageDTO1 = new CoinPackageDTO();
        coinPackageDTO1.setId(1L);
        CoinPackageDTO coinPackageDTO2 = new CoinPackageDTO();
        assertThat(coinPackageDTO1).isNotEqualTo(coinPackageDTO2);
        coinPackageDTO2.setId(coinPackageDTO1.getId());
        assertThat(coinPackageDTO1).isEqualTo(coinPackageDTO2);
        coinPackageDTO2.setId(2L);
        assertThat(coinPackageDTO1).isNotEqualTo(coinPackageDTO2);
        coinPackageDTO1.setId(null);
        assertThat(coinPackageDTO1).isNotEqualTo(coinPackageDTO2);
    }
}
