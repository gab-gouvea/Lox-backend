package br.com.lox.domain.reservation.dto;

import br.com.lox.domain.reservation.entity.FaxinaStatus;
import br.com.lox.domain.reservation.entity.ReservationSource;
import br.com.lox.domain.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CreateReservationDTO(
        @NotBlank String propriedadeId,
        @NotBlank String nomeHospede,
        @NotNull Instant checkIn,
        @NotNull Instant checkOut,
        @NotNull ReservationStatus status,
        @DecimalMin("0") BigDecimal precoTotal,
        String notas,
        @NotNull ReservationSource fonte,
        @NotNull @Min(1) Integer numHospedes,
        FaxinaStatus faxinaStatus,
        @NotNull Boolean faxinaPorMim,
        @DecimalMin("0") BigDecimal custoEmpresaFaxina,
        Boolean faxinaPaga,
        Instant faxinaData,
        List<DespesaDTO> despesas
) {}
