package com.example.cognit_invest.exceptions.user;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
    public TokenNotFoundException(){
        super("Token não encontrado!");
    }
}
