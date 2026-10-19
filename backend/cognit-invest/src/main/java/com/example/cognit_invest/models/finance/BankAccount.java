package com.example.cognit_invest.models.finance;

import com.example.cognit_invest.enums.bankAccount.Bank;
import com.example.cognit_invest.enums.bankAccount.ConnectionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "bank_account")
public class BankAccount {
    @Id
    private String id;

    private String userId;//id do usuário dono da conta

    private String name;//nome da conta
    private Bank bank;
    private ConnectionStatus status;

    private boolean syncEnabled;
    private String providerItemId;   // id da conexão no agregador
    private Instant consentExpiresAt;
    private Instant lastSyncAt;

    private Instant createdAt;
    private Instant updatedAi;
    private Instant deletedAt;
}
