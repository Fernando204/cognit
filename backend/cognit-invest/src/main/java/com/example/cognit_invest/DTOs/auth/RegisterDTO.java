package com.example.cognit_invest.DTOs.auth;

public record RegisterDTO(
        String name,
        String email,
        String phone,
        String document,
        String password
) {
}
