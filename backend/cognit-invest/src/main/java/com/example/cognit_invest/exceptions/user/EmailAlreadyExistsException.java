package com.example.cognit_invest.exceptions.user;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    public EmailAlreadyExistsException(){

        super("Email já cadastrado!");
    }
}
