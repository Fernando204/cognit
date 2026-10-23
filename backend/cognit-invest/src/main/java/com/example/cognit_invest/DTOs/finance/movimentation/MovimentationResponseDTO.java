package com.example.cognit_invest.DTOs.finance.movimentation;

import com.example.cognit_invest.enums.finance.MovimentationType;
import com.example.cognit_invest.enums.finance.PaymentWay;
import com.example.cognit_invest.models.finance.Movimentation;

import java.math.BigDecimal;
import java.time.Instant;

public record MovimentationResponseDTO(
        String id,
        String userId,
        String accountId,
        String category,
        String description,
        MovimentationType type,
        BigDecimal value,
        PaymentWay paymentWay,
        Instant createdAt,
        Instant updatedAt
) {

    public static MovimentationResponseDTO fromEntity(Movimentation movimentation) {
        return new MovimentationResponseDTO(
                movimentation.getId(),
                movimentation.getUserId(),
                movimentation.getAccountId(),
                movimentation.getCategory(),
                movimentation.getDescription(),
                movimentation.getType(),
                movimentation.getValue(),
                movimentation.getPaymentWay(),
                movimentation.getCreatedAt(),
                movimentation.getUpdatedAt()
        );
    }
}