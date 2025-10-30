package br.com.lox.domain.rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateRentalData(
        @NotBlank String tenantRental,
        @NotBlank String propertyId,
        @NotNull BigDecimal price,
        @NotNull Integer people,
        @NotNull LocalDateTime checkout,
        @NotNull LocalDateTime checkin
) {
}
