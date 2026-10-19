package com.example.cognit_invest.exceptions.user;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
    public InvalidCredentialsException(){
      super("Credenciais invalidas");
    }
}
