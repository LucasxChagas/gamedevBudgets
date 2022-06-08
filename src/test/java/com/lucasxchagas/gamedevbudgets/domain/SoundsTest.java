package com.lucasxchagas.gamedevbudgets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lucasxchagas.gamedevbudgets.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoundsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sounds.class);
        Sounds sounds1 = new Sounds();
        sounds1.setId(1L);
        Sounds sounds2 = new Sounds();
        sounds2.setId(sounds1.getId());
        assertThat(sounds1).isEqualTo(sounds2);
        sounds2.setId(2L);
        assertThat(sounds1).isNotEqualTo(sounds2);
        sounds1.setId(null);
        assertThat(sounds1).isNotEqualTo(sounds2);
    }
}
