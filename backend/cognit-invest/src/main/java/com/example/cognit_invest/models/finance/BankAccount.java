package com.example.cognit_invest.models.finance;

import com.example.cognit_invest.enums.bankAccount.Bank;
import com.example.cognit_invest.exceptions.finance.InsuficientCreditException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "bank_account")
public class BankAccount {
    @Id
    private String id;

    private String userId;//id do usuário dono da conta

    private String name;//nome da conta
    private Bank bank;

    private BigDecimal balance;

    //informações da relação de crédito, opcionais na hora da criação
    private boolean useCredit;
    private BigDecimal creditLimit = BigDecimal.ZERO;
    private BigDecimal usedCredit = BigDecimal.ZERO;

    private Integer invoiceClosing;
    private Integer invoiceDueDate;

    //informações de sincronização ao agregador para conectar ao banco, opcionais
    private boolean syncEnabled;
    private String providerItemId;   // id da conexão no agregador
    private Instant consentExpiresAt;
    private Instant lastSyncAt;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public BankAccount(
            String userId,
            String name,
            Bank bank,
            BigDecimal balance,

            boolean useCredit,
            BigDecimal creditLimit,
            BigDecimal usedCredit,

            Integer invoiceClosing,
            Integer invoiceDueDate
    ){
        this.userId = userId;
        this.name = name;
        this.bank = bank;
        this.balance = balance;
        this.useCredit = useCredit;
        this.creditLimit = creditLimit;
        this.usedCredit = usedCredit;
        this.invoiceClosing = invoiceClosing;
        this.invoiceDueDate = invoiceDueDate;

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Integer getInvoiceClosing() {
        return invoiceClosing;
    }

    public void setInvoiceClosing(Integer invoiceClosing) {
        this.invoiceClosing = invoiceClosing;
    }

    public Integer getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(Integer invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public boolean isUseCredit() {
        return useCredit;
    }

    public void setUseCredit(boolean useCredit) {
        this.useCredit = useCredit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getUsedCredit() {
        return usedCredit;
    }

    public void setUsedCredit(BigDecimal usedCredit) {
        this.usedCredit = usedCredit;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Instant getConsentExpiresAt() {
        return consentExpiresAt;
    }

    public void setConsentExpiresAt(Instant consentExpiresAt) {
        this.consentExpiresAt = consentExpiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(Instant lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderItemId() {
        return providerItemId;
    }

    public void setProviderItemId(String providerItemId) {
        this.providerItemId = providerItemId;
    }

    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(usedCredit);
    }

    public void consumeCredit(BigDecimal value){
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Value must be greater than zero");
        if(!useCredit || getAvailableCredit().compareTo(value) < 0) throw new InsuficientCreditException();
        usedCredit = usedCredit.add(value);
        updatedAt = Instant.now();
    }

    public void addToBalance(BigDecimal value){
        balance = balance.add(value);
        updatedAt = Instant.now();
    }
    public void removeFromBalance(BigDecimal value){
        balance = balance.subtract(value);
        updatedAt = Instant.now();
    }
}
