package br.com.lox.domain.scheduledmaintenance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ConfirmScheduledMaintenanceDTO(
        @DecimalMin("0") BigDecimal valor,
        @NotBlank String prestador
) {}
