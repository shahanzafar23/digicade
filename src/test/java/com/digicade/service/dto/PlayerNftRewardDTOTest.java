package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerNftRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerNftRewardDTO.class);
        PlayerNftRewardDTO playerNftRewardDTO1 = new PlayerNftRewardDTO();
        playerNftRewardDTO1.setId(1L);
        PlayerNftRewardDTO playerNftRewardDTO2 = new PlayerNftRewardDTO();
        assertThat(playerNftRewardDTO1).isNotEqualTo(playerNftRewardDTO2);
        playerNftRewardDTO2.setId(playerNftRewardDTO1.getId());
        assertThat(playerNftRewardDTO1).isEqualTo(playerNftRewardDTO2);
        playerNftRewardDTO2.setId(2L);
        assertThat(playerNftRewardDTO1).isNotEqualTo(playerNftRewardDTO2);
        playerNftRewardDTO1.setId(null);
        assertThat(playerNftRewardDTO1).isNotEqualTo(playerNftRewardDTO2);
    }
}
