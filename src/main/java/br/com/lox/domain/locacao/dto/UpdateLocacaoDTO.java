package br.com.lox.domain.locacao.dto;

import br.com.lox.domain.locacao.entity.LocacaoStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record UpdateLocacaoDTO(
        String propriedadeId,
        String nomeCompleto,
        String cpf,
        String rg,
        LocalDate dataNascimento,
        String profissao,
        String estadoCivil,
        String endereco,
        String email,
        Instant checkIn,
        Instant checkOut,
        BigDecimal valorMensal,
        String notas,
        LocacaoStatus status
) {}
