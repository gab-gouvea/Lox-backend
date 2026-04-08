package br.com.lox.domain.reservation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DespesaDTO(
        @NotBlank String descricao,
        @NotNull @DecimalMin("0") BigDecimal valor,
        @NotNull Boolean reembolsavel,
        Integer mes,
        Integer ano
) {}
