package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import com.lucasxchagas.gamedevbudgets.domain.Payment;
import com.lucasxchagas.gamedevbudgets.service.dto.BudgetDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.PaymentDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "budgets", source = "budgets", qualifiedByName = "budgetNameSet")
    PaymentDTO toDto(Payment s);

    @Mapping(target = "removeBudget", ignore = true)
    Payment toEntity(PaymentDTO paymentDTO);

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
