package com.example.cognit_invest.models.people;

import com.example.cognit_invest.enums.account.AccountStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String document;
    private String password;
    private AccountStatus status;

    private BigDecimal totalBalance = BigDecimal.ZERO;

    private Instant lastLogin;
    private Instant lastLogout;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public User(){}
    public User(
            String name,
            String email,
            String phone,
            String document,
            String password,
            AccountStatus status
    ){
        this.document = document;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.status = status;

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Instant getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(Instant lastLogout) {
        this.lastLogout = lastLogout;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addToBalance(BigDecimal value){
        totalBalance = totalBalance.add(value);
        updatedAt = Instant.now();
    }
    public void removeFromBalance(BigDecimal value){
        totalBalance = totalBalance.subtract(value);
        updatedAt = Instant.now();
    }

}
