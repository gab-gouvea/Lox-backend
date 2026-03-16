package br.com.lox.domain.maintenancerecord.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateMaintenanceRecordDTO(
        @NotBlank String propriedadeId,
        String componenteId,
        @NotBlank String nomeServico,
        String prestador,
        @NotNull LocalDate data,
        @NotNull @DecimalMin("0") BigDecimal valor,
        @NotNull Boolean pago
) {}
