package br.com.lox.domain.locacao.dto;

import br.com.lox.domain.locacao.entity.LocacaoStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record CreateLocacaoDTO(
        @NotBlank String propriedadeId,
        @NotBlank String nomeCompleto,
        String cpf,
        String rg,
        LocalDate dataNascimento,
        String profissao,
        String estadoCivil,
        String endereco,
        String email,
        @NotNull Instant checkIn,
        @NotNull Instant checkOut,
        Integer numMoradores,
        @DecimalMin("0") BigDecimal valorMensal,
        String garantia,
        Integer faxinaIntervaloDias,
        String notas,
        @NotNull LocacaoStatus status
) {}
