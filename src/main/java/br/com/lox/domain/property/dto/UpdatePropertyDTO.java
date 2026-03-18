package br.com.lox.domain.property.dto;

import br.com.lox.domain.property.entity.PropertyType;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdatePropertyDTO(
        String nome,
        String endereco,
        String proprietarioId,
        PropertyType tipo,
        Integer quartos,
        String fotoCapa,
        BigDecimal percentualComissao,
        BigDecimal taxaLimpeza,
        Boolean temHobbyBox,
        String acessoPredio,
        String acessoApartamento,
        Boolean ativo,
        Instant inativoAte,
        String observacaoInatividade
) {}
