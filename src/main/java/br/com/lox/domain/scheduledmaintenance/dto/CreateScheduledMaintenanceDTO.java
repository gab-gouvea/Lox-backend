package br.com.lox.domain.scheduledmaintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateScheduledMaintenanceDTO(
        @NotBlank String nome,
        @NotNull LocalDate dataPrevista,
        String prestador
) {}
