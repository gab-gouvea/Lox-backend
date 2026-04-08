package br.com.lox.domain.locacao.dto;

import br.com.lox.domain.locacao.entity.LocacaoStatus;
import br.com.lox.domain.reservation.dto.DespesaDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


public record UpdateLocacaoDTO(
        String propriedadeId,
        String tipoLocacao,
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
        Integer numMoradores,
        BigDecimal valorMensal,
        String tipoPagamento,
        BigDecimal valorTotal,
        BigDecimal percentualComissao,
        String garantia,
        Integer faxinaIntervaloDias,
        Instant ultimaFaxina,
        Instant proximaFaxina,
        String faxinaStatus,
        Boolean faxinaPorMim,
        BigDecimal custoEmpresaFaxina,
        Boolean faxinaPaga,
        Instant faxinaData,
        LocalDate vistoriaEntradaData,
        String vistoriaEntradaNotas,
        Boolean vistoriaEntradaConcluida,
        Boolean clearVistoriaEntrada,
        LocalDate vistoriaSaidaData,
        String vistoriaSaidaNotas,
        Boolean vistoriaSaidaConcluida,
        Boolean clearVistoriaSaida,
        List<DespesaDTO> despesas,
        String notas,
        LocacaoStatus status
) {}
