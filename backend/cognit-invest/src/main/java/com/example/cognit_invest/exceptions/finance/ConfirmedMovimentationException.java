package com.example.cognit_invest.exceptions.finance;

public class ConfirmedMovimentationException extends RuntimeException{
    public ConfirmedMovimentationException(){
        super("Movimentação já foi confirmada!");
    }
}
