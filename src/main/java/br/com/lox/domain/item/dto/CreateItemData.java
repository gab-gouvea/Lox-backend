package br.com.lox.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateItemData(
        @NotBlank String name,
        @NotNull Integer quantity,
        String imageUrl,
        @NotNull BigDecimal price,
        @NotNull String inventoryId,
        @NotNull String categoryId
) {
}
