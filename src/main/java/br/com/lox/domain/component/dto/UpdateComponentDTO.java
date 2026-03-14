package br.com.lox.domain.component.dto;

import java.time.LocalDate;

public record UpdateComponentDTO(
        String nome,
        LocalDate ultimaManutencao,
        LocalDate proximaManutencao,
        Integer intervaloDias,
        String prestador,
        String observacoes
) {}
