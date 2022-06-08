package com.lucasxchagas.gamedevbudgets.service.mapper;

import com.lucasxchagas.gamedevbudgets.domain.Budget;
import com.lucasxchagas.gamedevbudgets.domain.Game;
import com.lucasxchagas.gamedevbudgets.domain.Payment;
import com.lucasxchagas.gamedevbudgets.service.dto.BudgetDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.GameDTO;
import com.lucasxchagas.gamedevbudgets.service.dto.PaymentDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Budget} and its DTO {@link BudgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {
    @Mapping(target = "payments", source = "payments", qualifiedByName = "paymentPaymentTypeSet")
    @Mapping(target = "game", source = "game", qualifiedByName = "gameName")
    BudgetDTO toDto(Budget s);

    @Mapping(target = "removePayment", ignore = true)
    Budget toEntity(BudgetDTO budgetDTO);

    @Named("paymentPaymentType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "paymentType", source = "paymentType")
    PaymentDTO toDtoPaymentPaymentType(Payment payment);

    @Named("paymentPaymentTypeSet")
    default Set<PaymentDTO> toDtoPaymentPaymentTypeSet(Set<Payment> payment) {
        return payment.stream().map(this::toDtoPaymentPaymentType).collect(Collectors.toSet());
    }

    @Named("gameName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GameDTO toDtoGameName(Game game);
}
