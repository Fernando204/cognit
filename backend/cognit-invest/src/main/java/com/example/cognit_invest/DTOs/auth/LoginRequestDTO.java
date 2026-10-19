package com.example.cognit_invest.DTOs.auth;

public record LoginRequestDTO(
        String email,
        String password
) {
}
