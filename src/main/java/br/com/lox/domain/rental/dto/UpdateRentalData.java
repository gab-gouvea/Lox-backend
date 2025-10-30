package br.com.lox.domain.rental.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateRentalData(
        String tenantName,
        String propertyId,
        BigDecimal price,
        Integer people,
        LocalDateTime checkout,
        LocalDateTime checkin,
        String inventoryId
) {
}
