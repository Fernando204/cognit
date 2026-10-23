package com.example.cognit_invest.DTOs.finance.bankAccount;

import com.example.cognit_invest.enums.bankAccount.Bank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BankAccountRequestDTO(

        @NotBlank(message = "O nome da conta é obrigatório")
        @Size(max = 100, message = "O nome da conta deve ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "O banco é obrigatório")
        Bank bank,

        @NotNull(message = "O saldo é obrigatório")
        BigDecimal balance,

        boolean useCredit,

        @DecimalMin(value = "0.0", message = "O limite de crédito não pode ser negativo")
        BigDecimal creditLimit,
        @DecimalMin(value = "0.0", message = "O crédito utilizado não pode ser negativo")
        BigDecimal usedCredit,
        //datas  de fechamento e vencimento da fatura, podendo ser de 1-31
        Integer invoiceClosing,
        Integer invoiceDueDate

) {
}
