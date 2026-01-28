package br.com.lox.domain.inventory.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateInventoryData(
        @NotNull List<String> itemsId
        ) {
}
