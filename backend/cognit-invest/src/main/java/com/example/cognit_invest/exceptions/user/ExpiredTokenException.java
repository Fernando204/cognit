package com.example.cognit_invest.exceptions.user;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }
    public ExpiredTokenException(){super("Token Expirado!");}
}
