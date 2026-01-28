package br.com.lox.domain.inventory.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AddItemsData(
        @NotNull List<String> itemsId
) {
}
