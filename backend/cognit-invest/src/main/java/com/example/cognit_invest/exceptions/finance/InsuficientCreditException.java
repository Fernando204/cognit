package com.example.cognit_invest.exceptions.finance;

public class InsuficientCreditException extends RuntimeException {
    public InsuficientCreditException(String message) {
        super(message);
    }
    public InsuficientCreditException(){
      super("Limite insuficiente!");
    }
}
