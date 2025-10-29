package br.com.lox.domain.component.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateComponentData(
        String name,
        LocalDateTime lastMaintenance,
        LocalDateTime nextMaintenance,
        Integer maintenanceDays,
        BigDecimal maintenanceCost,
        String notes
) {
}
