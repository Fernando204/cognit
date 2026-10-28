package com.example.cognit_invest.models.finance;

import com.example.cognit_invest.enums.finance.MovimentationStatus;
import com.example.cognit_invest.enums.finance.MovimentationType;
import com.example.cognit_invest.enums.finance.PaymentWay;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "movimentations")
public class Movimentation {
    @Id
    private String id;

    private String userId;
    private BigDecimal value;
    private MovimentationType type;
    private String category;
    private String description;
    private String accountId;
    private PaymentWay paymentWay;
    private MovimentationStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Movimentation(){}
    public Movimentation(
            String accountId,
            String category,
            String description,
            MovimentationType type,
            String userId,
            BigDecimal value,
            PaymentWay paymentWay
    ) {
        this.accountId = accountId;
        this.category = category;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.value = value;
        this.paymentWay = paymentWay;

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public MovimentationStatus getStatus() {
        return status;
    }

    public void setStatus(MovimentationStatus status) {
        this.status = status;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public PaymentWay getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(PaymentWay paymentWay) {
        this.paymentWay = paymentWay;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MovimentationType getType() {
        return type;
    }

    public void setType(MovimentationType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
