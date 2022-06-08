package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sounds} and its DTO {@link SoundsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SoundsMapper extends EntityMapper<SoundsDTO, Sounds> {}
