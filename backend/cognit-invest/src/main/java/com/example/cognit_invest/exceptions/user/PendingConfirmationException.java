package com.example.cognit_invest.exceptions.user;

public class PendingConfirmationException extends RuntimeException {
    private String code;

    public PendingConfirmationException(String message) {
        super(message);
    }
    private PendingConfirmationException(String code,String message){
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PendingConfirmationException pendingEmailConfirmation(){
        return new PendingConfirmationException(
                "PENDING_EMAIL",
                "Confirmação de Email pendente"
        );
    }
}
