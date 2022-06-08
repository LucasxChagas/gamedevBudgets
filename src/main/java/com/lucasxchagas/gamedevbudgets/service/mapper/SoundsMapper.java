package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import com.lucasxchagas.gamedevbudgets.service.dto.BudgetDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sounds} and its DTO {@link SoundsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SoundsMapper extends EntityMapper<SoundsDTO, Sounds> {
    @Mapping(target = "budgets", source = "budgets", qualifiedByName = "budgetNameSet")
    SoundsDTO toDto(Sounds s);

    @Mapping(target = "removeBudget", ignore = true)
    Sounds toEntity(SoundsDTO soundsDTO);

    @Named("budgetName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BudgetDTO toDtoBudgetName(Budget budget);

    @Named("budgetNameSet")
    default Set<BudgetDTO> toDtoBudgetNameSet(Set<Budget> budget) {
        return budget.stream().map(this::toDtoBudgetName).collect(Collectors.toSet());
    }
}
