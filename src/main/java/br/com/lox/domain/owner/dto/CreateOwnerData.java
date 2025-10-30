package br.com.lox.domain.owner.dto;

import br.com.lox.domain.property.entity.Property;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateOwnerData(
        @NotBlank String name,
        @NotBlank String cpf,
        @NotBlank String email,
        String phone,
        List<String> propertiesId
) {
}
