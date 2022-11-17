package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NftRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NftReward.class);
        NftReward nftReward1 = new NftReward();
        nftReward1.setId(1L);
        NftReward nftReward2 = new NftReward();
        nftReward2.setId(nftReward1.getId());
        assertThat(nftReward1).isEqualTo(nftReward2);
        nftReward2.setId(2L);
        assertThat(nftReward1).isNotEqualTo(nftReward2);
        nftReward1.setId(null);
        assertThat(nftReward1).isNotEqualTo(nftReward2);
    }
}
