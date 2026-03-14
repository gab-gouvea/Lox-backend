package br.com.lox.domain.inventoryitem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInventoryItemDTO(
        @NotBlank String comodo,
        @NotBlank String nome,
        @NotNull @Min(0) Integer quantidade,
        String descricao,
        String imagemUrl
) {}
