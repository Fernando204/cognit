package com.example.cognit_invest.models.auth;

import com.example.cognit_invest.enums.auth.LoginStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "login_register")
public class LoginRegister {
    @Id
    private String id;

    private String email;
    private LoginStatus status;
    private String ipAddress;
    private String userAgent;

    private Instant createdAt;

    public LoginRegister(){}
    public LoginRegister(
            String email,
            String ipAddress,
            LoginStatus status,
            String userAgent
        ) {
        this.email = email;
        this.ipAddress = ipAddress;
        this.status = status;
        this.userAgent = userAgent;

        this.createdAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
