package br.com.lox.domain.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateInventoryData(
        @NotNull List<String> itemsId,
        @NotNull LocalDate date,
        @NotBlank String responsible
) {
}
