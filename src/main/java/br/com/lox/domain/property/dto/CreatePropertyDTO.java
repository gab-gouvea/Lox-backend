package br.com.lox.domain.property.dto;

import br.com.lox.domain.property.entity.PropertyType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePropertyDTO(
        @NotBlank String nome,
        String endereco,
        String proprietarioId,
        @NotNull PropertyType tipo,
        @NotNull @Min(0) Integer quartos,
        String fotoCapa,
        @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal percentualComissao,
        @DecimalMin("0") BigDecimal taxaLimpeza,
        @NotNull Boolean temHobbyBox,
        String acessoPredio,
        String acessoApartamento,
        String senhaWifi,
        @NotNull Boolean ativo
) {}
