package br.com.lox.domain.scheduledmaintenance.dto;

import java.time.LocalDate;

public record UpdateScheduledMaintenanceDTO(
        String nome,
        LocalDate dataPrevista,
        String prestador
) {}
