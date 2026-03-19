package br.com.lox.domain.reservation.dto;

import br.com.lox.domain.reservation.entity.FaxinaStatus;
import br.com.lox.domain.reservation.entity.ReservationSource;
import br.com.lox.domain.reservation.entity.ReservationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record UpdateReservationDTO(
        String propriedadeId,
        String nomeHospede,
        Instant checkIn,
        Instant checkOut,
        ReservationStatus status,
        BigDecimal precoTotal,
        String notas,
        ReservationSource fonte,
        Integer numHospedes,
        FaxinaStatus faxinaStatus,
        Boolean faxinaPorMim,
        BigDecimal custoEmpresaFaxina,
        Boolean faxinaPaga,
        Instant faxinaData,
        List<DespesaDTO> despesas,
        BigDecimal valorRecebidoCancelamento,
        BigDecimal valorLiquidoCancelamento,
        Boolean pagamentoRecebido
) {}
