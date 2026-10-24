package com.example.cognit_invest.DTOs.auth;

public record ChangePasswordDTO(
        String code,//código do token é enviado novamente para confirmação
        String email,//email do usuário
        String password//nova senha
){
}
