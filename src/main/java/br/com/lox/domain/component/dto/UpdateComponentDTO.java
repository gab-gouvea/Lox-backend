package br.com.lox.domain.component.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateComponentDTO(
        String nome,
        LocalDate ultimaManutencao,
        LocalDate proximaManutencao,
        Integer intervaloDias,
        @NotBlank String prestador,
        String observacoes
) {}
