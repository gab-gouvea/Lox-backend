package br.com.lox.domain.locacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ParcelaTaxaDTO(
        @Min(1) @Max(31) Integer dia,
        @NotNull @Min(1) @Max(12) Integer mes,
        @NotNull Integer ano,
        @NotNull @DecimalMin("0") BigDecimal valor
) {}
