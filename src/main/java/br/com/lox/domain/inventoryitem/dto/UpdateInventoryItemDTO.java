package br.com.lox.domain.inventoryitem.dto;

public record UpdateInventoryItemDTO(
        String comodo,
        String nome,
        Integer quantidade,
        String descricao,
        String imagemUrl
) {}
