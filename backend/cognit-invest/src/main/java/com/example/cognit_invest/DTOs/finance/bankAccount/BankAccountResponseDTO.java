package com.example.cognit_invest.DTOs.finance.bankAccount;

import com.example.cognit_invest.enums.bankAccount.Bank;
import com.example.cognit_invest.models.finance.BankAccount;

import java.math.BigDecimal;
import java.time.Instant;

public record BankAccountResponseDTO(

        String id,
        String userId,
        String name,
        Bank bank,

        BigDecimal balance,

        boolean useCredit,
        BigDecimal creditLimit,
        BigDecimal usedCredit,

        Integer invoiceClosing,
        Integer invoiceDUeDate,

        boolean syncEnabled,
        String providerItemId,
        Instant consentExpiresAt,
        Instant lastSyncAt,

        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt

) {

    public static BankAccountResponseDTO fromEntity(BankAccount entity) {
        if (entity == null) {
            return null;
        }

        return new BankAccountResponseDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getBank(),

                entity.getBalance(),

                entity.isUseCredit(),
                entity.getCreditLimit(),
                entity.getUsedCredit(),

                entity.getInvoiceClosing(),
                entity.getInvoiceDueDate(),

                entity.isSyncEnabled(),
                entity.getProviderItemId(),
                entity.getConsentExpiresAt(),
                entity.getLastSyncAt(),

                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
