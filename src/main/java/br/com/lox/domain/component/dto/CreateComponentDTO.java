package br.com.lox.domain.component.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateComponentDTO(
        @NotBlank String nome,
        @NotNull LocalDate ultimaManutencao,
        LocalDate proximaManutencao,
        @NotNull @Min(1) Integer intervaloDias,
        @NotBlank String prestador,
        String observacoes
) {}
