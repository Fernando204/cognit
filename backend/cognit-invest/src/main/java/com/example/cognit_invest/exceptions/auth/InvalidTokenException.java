package com.example.cognit_invest.exceptions.auth;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(){
        super("Token invalido!");
    }
}
