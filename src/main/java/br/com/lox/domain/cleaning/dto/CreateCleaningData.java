package br.com.lox.domain.cleaning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCleaningData(
        @NotBlank String rentalId,
        @NotBlank String tenantName,
        @NotNull BigDecimal price,
        BigDecimal cleaningTax,
        LocalDate date,
        String notes
        ) {
}
