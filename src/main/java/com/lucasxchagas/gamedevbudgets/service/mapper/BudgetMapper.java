package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import com.lucasxchagas.gamedevbudgets.domain.Game;
import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import com.lucasxchagas.gamedevbudgets.service.dto.BudgetDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.GameDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Budget} and its DTO {@link BudgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {
    @Mapping(target = "sounds", source = "sounds", qualifiedByName = "soundsNameSet")
    @Mapping(target = "game", source = "game", qualifiedByName = "gameName")
    BudgetDTO toDto(Budget s);

    @Mapping(target = "removeSounds", ignore = true)
    Budget toEntity(BudgetDTO budgetDTO);

    @Named("soundsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SoundsDTO toDtoSoundsName(Sounds sounds);

    @Named("soundsNameSet")
    default Set<SoundsDTO> toDtoSoundsNameSet(Set<Sounds> sounds) {
        return sounds.stream().map(this::toDtoSoundsName).collect(Collectors.toSet());
    }

    @Named("gameName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GameDTO toDtoGameName(Game game);
}
