package com.example.cognit_invest.models.auth;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Document(collection = "recover_token")
public class RecoverToken {
    @Id
    private String id;

    private String userId;
    private String email;
    private String code;
    private Instant expiresAt;
    private Instant createdAt;

    public RecoverToken(String code, String email, String userId) {
        this.code = code;
        this.email = email;
        this.userId = userId;

        this.createdAt = Instant.now();
        this.expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

