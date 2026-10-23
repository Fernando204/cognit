package com.example.cognit_invest.DTOs.finance.bankAccount;

import java.math.BigDecimal;

public record EditCreditDTO(
        String accountId,
        boolean useCredit,
        BigDecimal creditLimit,
        Integer invoiceClosing,
        Integer invoiceDueDate
) {
}
