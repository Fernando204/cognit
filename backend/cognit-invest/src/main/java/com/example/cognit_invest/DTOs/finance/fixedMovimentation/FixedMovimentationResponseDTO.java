package com.example.cognit_invest.DTOs.finance.fixedMovimentation;

import com.example.cognit_invest.enums.DayOfWeek;
import com.example.cognit_invest.enums.Month;
import com.example.cognit_invest.enums.finance.FixedMovimentationPeriod;
import com.example.cognit_invest.enums.finance.MovimentationType;
import com.example.cognit_invest.models.finance.FixedMovimentation;

import java.math.BigDecimal;
import java.time.Instant;

public record FixedMovimentationResponseDTO(
        String id,
        String title,
        String description,
        String category,
        BigDecimal value,
        String accountId,
        MovimentationType type,
        FixedMovimentationPeriod period,
        Month month,
        Integer dayOfMonth,
        DayOfWeek dayOfWeek,
        Instant lastCreation,
        Instant nextCreation,
        Instant createdAt,
        Instant updatedAt
) {
    public static FixedMovimentationResponseDTO from(FixedMovimentation fm) {
        return new FixedMovimentationResponseDTO(
                fm.getId(),
                fm.getTitle(),
                fm.getDescription(),
                fm.getCategory(),
                fm.getValue(),
                fm.getAccountId(),
                fm.getType(),
                fm.getPeriod(),
                fm.getMonth(),
                fm.getDayOfMonth(),
                fm.getDayOfWeek(),
                fm.getLastCreation(),
                fm.getNextCreation(),
                fm.getCreatedAt(),
                fm.getUpdatedAt()
        );
    }
}