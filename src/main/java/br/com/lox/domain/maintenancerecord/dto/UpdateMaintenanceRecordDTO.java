package br.com.lox.domain.maintenancerecord.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateMaintenanceRecordDTO(
        String propriedadeId,
        String componenteId,
        String nomeServico,
        String prestador,
        LocalDate data,
        BigDecimal valor,
        Boolean pago
) {}
