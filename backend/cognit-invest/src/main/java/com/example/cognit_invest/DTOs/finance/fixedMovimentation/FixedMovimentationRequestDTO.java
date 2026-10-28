package com.example.cognit_invest.DTOs.finance.fixedMovimentation;

import com.example.cognit_invest.enums.DayOfWeek;
import com.example.cognit_invest.enums.Month;
import com.example.cognit_invest.enums.finance.FixedMovimentationPeriod;
import com.example.cognit_invest.enums.finance.MovimentationType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record FixedMovimentationRequestDTO(
        @NotBlank String title,
        String description,
        @NotBlank String category,
        @NotNull @Positive BigDecimal value,
        @NotBlank String accountId,
        @NotNull MovimentationType type,
        @NotNull FixedMovimentationPeriod period,
        Month month,
        @Min(1) @Max(31) Integer dayOfMonth,
        DayOfWeek dayOfWeek
) {}