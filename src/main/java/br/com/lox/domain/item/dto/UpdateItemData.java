package br.com.lox.domain.item.dto;

import java.math.BigDecimal;

public record UpdateItemData(
        String name,
        Integer quantity,
        String imageUrl,
        BigDecimal price,
        String categoryId,
        String inventoryId
){
}
