package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import com.lucasxchagas.gamedevbudgets.service.dto.BudgetDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sounds} and its DTO {@link SoundsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SoundsMapper extends EntityMapper<SoundsDTO, Sounds> {
    @Mapping(target = "bugdet", source = "bugdet", qualifiedByName = "budgetName")
    SoundsDTO toDto(Sounds s);

    @Named("budgetName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BudgetDTO toDtoBudgetName(Budget budget);
}
