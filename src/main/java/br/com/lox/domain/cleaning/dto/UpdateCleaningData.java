package br.com.lox.domain.cleaning.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateCleaningData(
        String rentalId,
        String tenantName,
        BigDecimal price,
        BigDecimal cleaningTax,
        LocalDate date,
        String notes
) {
}
