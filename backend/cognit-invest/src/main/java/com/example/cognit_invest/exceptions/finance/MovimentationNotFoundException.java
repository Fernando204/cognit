package com.example.cognit_invest.exceptions.finance;

public class MovimentationNotFoundException extends RuntimeException {
    public MovimentationNotFoundException(String message) {
        super(message);
    }
    public MovimentationNotFoundException(){
        super("Movimentação não encontrada!");
    }
}
