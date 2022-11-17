package com.digicade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DigiUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DigiUserDTO.class);
        DigiUserDTO digiUserDTO1 = new DigiUserDTO();
        digiUserDTO1.setId(1L);
        DigiUserDTO digiUserDTO2 = new DigiUserDTO();
        assertThat(digiUserDTO1).isNotEqualTo(digiUserDTO2);
        digiUserDTO2.setId(digiUserDTO1.getId());
        assertThat(digiUserDTO1).isEqualTo(digiUserDTO2);
        digiUserDTO2.setId(2L);
        assertThat(digiUserDTO1).isNotEqualTo(digiUserDTO2);
        digiUserDTO1.setId(null);
        assertThat(digiUserDTO1).isNotEqualTo(digiUserDTO2);
    }
}
