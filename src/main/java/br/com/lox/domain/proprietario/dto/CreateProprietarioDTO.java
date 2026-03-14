package br.com.lox.domain.proprietario.dto;

import br.com.lox.domain.proprietario.entity.EstadoCivil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateProprietarioDTO(
        @NotBlank String nomeCompleto,
        @NotBlank @Size(min = 11) String cpf,
        String rg,
        LocalDate dataNascimento,
        String profissao,
        EstadoCivil estadoCivil,
        String endereco,
        @Email String email
) {}
