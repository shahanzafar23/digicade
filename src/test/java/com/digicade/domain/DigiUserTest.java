package com.digicade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.digicade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DigiUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DigiUser.class);
        DigiUser digiUser1 = new DigiUser();
        digiUser1.setId(1L);
        DigiUser digiUser2 = new DigiUser();
        digiUser2.setId(digiUser1.getId());
        assertThat(digiUser1).isEqualTo(digiUser2);
        digiUser2.setId(2L);
        assertThat(digiUser1).isNotEqualTo(digiUser2);
        digiUser1.setId(null);
        assertThat(digiUser1).isNotEqualTo(digiUser2);
    }
}
