package com.example.cognit_invest.exceptions.user;

public class DisabledAccountException extends RuntimeException {
    public DisabledAccountException(String message) {
        super(message);
    }
    public DisabledAccountException(){
        super("Conta inativa");
    }

}
