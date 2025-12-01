package br.com.lox.domain.owner.dto;


import jakarta.validation.constraints.NotBlank;


public record CreateOwnerData(
        @NotBlank String name,
        @NotBlank String cpf,
        @NotBlank String email,
        String phone
) {
}
