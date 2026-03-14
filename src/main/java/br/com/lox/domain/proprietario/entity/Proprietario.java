package br.com.lox.domain.proprietario.entity;

import br.com.lox.domain.proprietario.dto.UpdateProprietarioDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "proprietarios")
@NoArgsConstructor
@Getter
public class Proprietario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    private String rg;

    private LocalDate dataNascimento;

    private String profissao;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    private String endereco;

    private String email;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;

    public Proprietario(String nomeCompleto, String cpf, String rg, LocalDate dataNascimento,
                        String profissao, EstadoCivil estadoCivil, String endereco, String email) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.profissao = profissao;
        this.estadoCivil = estadoCivil;
        this.endereco = endereco;
        this.email = email;
        this.criadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    public void updateValues(UpdateProprietarioDTO data) {
        this.atualizadoEm = Instant.now();
        if (data.nomeCompleto() != null) this.nomeCompleto = data.nomeCompleto();
        if (data.cpf() != null) this.cpf = data.cpf();
        if (data.rg() != null) this.rg = data.rg();
        if (data.dataNascimento() != null) this.dataNascimento = data.dataNascimento();
        if (data.profissao() != null) this.profissao = data.profissao();
        if (data.estadoCivil() != null) this.estadoCivil = data.estadoCivil();
        if (data.endereco() != null) this.endereco = data.endereco();
        if (data.email() != null) this.email = data.email();
    }
}
