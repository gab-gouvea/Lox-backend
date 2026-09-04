package br.com.lox.domain.locacao.dto;

import br.com.lox.domain.locacao.entity.LocacaoStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record CreateLocacaoDTO(
        @NotBlank String propriedadeId,
        String tipoLocacao,
        @NotBlank String nomeCompleto,
        String cpf,
        String rg,
        LocalDate dataNascimento,
        String profissao,
        String estadoCivil,
        String endereco,
        String email,
        Boolean incluirConjuge,
        String conjugeNome,
        String conjugeCpf,
        String conjugeRg,
        LocalDate conjugeDataNascimento,
        String conjugeProfissao,
        String conjugeEstadoCivil,
        String conjugeEndereco,
        String conjugeEmail,
        @NotNull Instant checkIn,
        @NotNull Instant checkOut,
        Integer numMoradores,
        @DecimalMin("0") BigDecimal valorMensal,
        String tipoPagamento,
        @DecimalMin("0") BigDecimal valorTotal,
        @DecimalMin("0") BigDecimal percentualComissao,
        @DecimalMin("0") BigDecimal taxaLimpeza,
        String garantia,
        Boolean semAdministracao,
        @DecimalMin("0") @DecimalMax("100") BigDecimal percentualPrimeiroAluguel,
        Integer mesTaxa,
        Integer anoTaxa,
        @Valid List<ParcelaTaxaDTO> parcelasTaxa,
        Integer faxinaIntervaloDias,
        String notas,
        @NotNull LocacaoStatus status
) {}
