package com.example.cognit_invest.DTOs.finance.movimentation;

import com.example.cognit_invest.enums.finance.PaymentWay;

public record ConfirmMovimentationDTO(
        String movimentationId,
        PaymentWay paymentWay
) {
}
