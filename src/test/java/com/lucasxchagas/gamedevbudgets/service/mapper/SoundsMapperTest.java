package com.lucasxchagas.gamedevbudgets.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoundsMapperTest {

    private SoundsMapper soundsMapper;

    @BeforeEach
    public void setUp() {
        soundsMapper = new SoundsMapperImpl();
    }
}
