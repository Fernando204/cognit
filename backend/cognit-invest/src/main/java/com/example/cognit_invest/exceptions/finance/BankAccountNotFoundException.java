package com.example.cognit_invest.exceptions.finance;

public class BankAccountNotFoundException extends RuntimeException{
    public BankAccountNotFoundException(){
        super("Conta bancaria não encontrada!");
    }
}
