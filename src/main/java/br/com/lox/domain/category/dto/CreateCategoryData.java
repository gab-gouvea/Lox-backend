package br.com.lox.domain.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryData(
       @NotBlank String name,
       @NotBlank String color
) {
}
