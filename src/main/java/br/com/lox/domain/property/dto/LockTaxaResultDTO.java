package br.com.lox.domain.property.dto;

import java.math.BigDecimal;

public record LockTaxaResultDTO(
        int reservationsLocked,
        int locacoesLocked,
        BigDecimal taxaLimpezaAplicada
) {}
