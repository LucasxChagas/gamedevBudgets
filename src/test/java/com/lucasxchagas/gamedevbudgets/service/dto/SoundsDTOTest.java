package com.lucasxchagas.gamedevbudgets.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lucasxchagas.gamedevbudgets.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoundsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoundsDTO.class);
        SoundsDTO soundsDTO1 = new SoundsDTO();
        soundsDTO1.setId(1L);
        SoundsDTO soundsDTO2 = new SoundsDTO();
        assertThat(soundsDTO1).isNotEqualTo(soundsDTO2);
        soundsDTO2.setId(soundsDTO1.getId());
        assertThat(soundsDTO1).isEqualTo(soundsDTO2);
        soundsDTO2.setId(2L);
        assertThat(soundsDTO1).isNotEqualTo(soundsDTO2);
        soundsDTO1.setId(null);
        assertThat(soundsDTO1).isNotEqualTo(soundsDTO2);
    }
}
