package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NftRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NftRewardDTO.class);
        NftRewardDTO nftRewardDTO1 = new NftRewardDTO();
        nftRewardDTO1.setId(1L);
        NftRewardDTO nftRewardDTO2 = new NftRewardDTO();
        assertThat(nftRewardDTO1).isNotEqualTo(nftRewardDTO2);
        nftRewardDTO2.setId(nftRewardDTO1.getId());
        assertThat(nftRewardDTO1).isEqualTo(nftRewardDTO2);
        nftRewardDTO2.setId(2L);
        assertThat(nftRewardDTO1).isNotEqualTo(nftRewardDTO2);
        nftRewardDTO1.setId(null);
        assertThat(nftRewardDTO1).isNotEqualTo(nftRewardDTO2);
    }
}
