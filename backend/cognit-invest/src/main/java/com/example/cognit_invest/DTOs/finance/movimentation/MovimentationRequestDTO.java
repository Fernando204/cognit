package com.example.cognit_invest.DTOs.finance.movimentation;

import com.example.cognit_invest.enums.finance.MovimentationStatus;
import com.example.cognit_invest.enums.finance.MovimentationType;
import com.example.cognit_invest.enums.finance.PaymentWay;
import com.example.cognit_invest.models.finance.Movimentation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MovimentationRequestDTO(
        @NotBlank(message = "accountId is required")
        String accountId,

        @NotBlank(message = "category is required")
        String category,

        String description,

        @NotNull(message = "type is required")
        MovimentationType type,

        @NotNull(message = "value is required")
        @Positive(message = "value must be greater than zero")
        BigDecimal value,

        @NotNull(message = "paymentWay is required")
        PaymentWay paymentWay
) {

    /**
     * O userId não vem no body: deve ser extraído do usuário autenticado.
     */
    public Movimentation toEntity(String userId) {
        return new Movimentation(
                accountId,
                category,
                description,
                type,
                userId,
                value,
                paymentWay
        );
    }

    /**
     * Aplica os campos do request em uma movimentação existente (update).
     */
    public void updateEntity(Movimentation movimentation) {
        movimentation.setAccountId(accountId);
        movimentation.setCategory(category);
        movimentation.setDescription(description);
        movimentation.setType(type);
        movimentation.setValue(value);
        movimentation.setPaymentWay(paymentWay);
        movimentation.setUpdatedAt(java.time.Instant.now());
    }
}
