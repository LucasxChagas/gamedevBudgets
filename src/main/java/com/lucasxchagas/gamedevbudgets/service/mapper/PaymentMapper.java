package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import com.lucasxchagas.gamedevbudgets.domain.Payment;
import com.lucasxchagas.gamedevbudgets.service.dto.BudgetDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "budgetId")
    PaymentDTO toDto(Payment s);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);
}
