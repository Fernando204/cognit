package com.example.cognit_invest.DTOs.auth;

import com.example.cognit_invest.enums.account.AccountStatus;
import com.example.cognit_invest.models.people.User;

public record LoginResponseDTO(
        String id,
        String name,
        String email,
        String document,
        AccountStatus status
) {
    public static LoginResponseDTO fromEntity(User user){
        return new LoginResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDocument(),
                user.getStatus()
        );
    }
}
