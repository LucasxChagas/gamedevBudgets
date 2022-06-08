package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Game;
import com.lucasxchagas.gamedevbudgets.service.dto.GameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameMapper extends EntityMapper<GameDTO, Game> {}
