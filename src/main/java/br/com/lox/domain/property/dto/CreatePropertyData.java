package br.com.lox.domain.property.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;


public record CreatePropertyData(
        @NotBlank
        String title,
        @NotBlank
        String address,
        Map<Integer , String> photos,
        @NotNull()
        List<String> componentsId,
        @NotBlank
        String ownerId,
        String notes,
        String conciergeCode,
        String doorCode
) {
}
