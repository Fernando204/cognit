package com.example.cognit_invest.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(){
        super("Usuário não encontrado!");
    }
}
