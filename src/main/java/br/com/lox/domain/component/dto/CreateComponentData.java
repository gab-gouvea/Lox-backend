package br.com.lox.domain.component.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateComponentData(
        @NotBlank String name,
        LocalDateTime lastMaintenance,
        LocalDateTime nextMaintenance,
        Integer maintenanceDays,
        BigDecimal maintenanceCost,
        String notes
        ) {
}
