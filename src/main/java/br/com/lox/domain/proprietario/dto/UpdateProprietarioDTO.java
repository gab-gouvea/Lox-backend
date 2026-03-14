package br.com.lox.domain.proprietario.dto;

import br.com.lox.domain.proprietario.entity.EstadoCivil;

import java.time.LocalDate;

public record UpdateProprietarioDTO(
        String nomeCompleto,
        String cpf,
        String rg,
        LocalDate dataNascimento,
        String profissao,
        EstadoCivil estadoCivil,
        String endereco,
        String email
) {}
