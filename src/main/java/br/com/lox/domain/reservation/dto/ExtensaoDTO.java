package br.com.lox.domain.reservation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record ExtensaoDTO(
        @NotNull Instant dataInicio,
        @NotNull @DecimalMin("0") BigDecimal valor
) {}
