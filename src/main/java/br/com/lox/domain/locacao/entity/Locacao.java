package br.com.lox.domain.locacao.entity;

import br.com.lox.domain.locacao.dto.UpdateLocacaoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "locacoes")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Locacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;

    @Column(nullable = false)
    private String nomeCompleto;

    private String cpf;

    private String rg;

    private LocalDate dataNascimento;

    private String profissao;

    private String estadoCivil;

    private String endereco;

    private String email;

    @Column(nullable = false)
    private Instant checkIn;

    @Column(nullable = false)
    private Instant checkOut;

    private Integer numMoradores;

    private BigDecimal valorMensal;

    private String tipoPagamento; // "avista" ou "mensal"

    private BigDecimal valorTotal; // usado quando tipoPagamento = "avista"

    private BigDecimal percentualComissao;

    private String garantia;

    // Faxina de rotina
    private Integer faxinaIntervaloDias;
    private Instant ultimaFaxina;
    private Instant proximaFaxina;

    // Faxina de saída (mesmos campos das reservas)
    private String faxinaStatus; // "nao_agendada", "agendada"
    private Boolean faxinaPorMim;
    private BigDecimal custoEmpresaFaxina;
    private Boolean faxinaPaga;
    private Instant faxinaData;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocacaoStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant atualizadoEm;

    public Locacao(String propriedadeId, String nomeCompleto, String cpf, String rg,
                   LocalDate dataNascimento, String profissao, String estadoCivil,
                   String endereco, String email, Instant checkIn, Instant checkOut,
                   Integer numMoradores, BigDecimal valorMensal, String garantia,
                   Integer faxinaIntervaloDias, String notas, LocacaoStatus status) {
        this.propriedadeId = propriedadeId;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.profissao = profissao;
        this.estadoCivil = estadoCivil;
        this.endereco = endereco;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numMoradores = numMoradores;
        this.valorMensal = valorMensal;
        this.garantia = garantia;
        this.faxinaIntervaloDias = faxinaIntervaloDias;
        this.notas = notas;
        this.status = status;
    }

    public void setStatus(LocacaoStatus status) {
        this.status = status;
    }

    public void updateValues(UpdateLocacaoDTO data) {
        if (data.propriedadeId() != null) this.propriedadeId = data.propriedadeId();
        if (data.nomeCompleto() != null) this.nomeCompleto = data.nomeCompleto();
        if (data.cpf() != null) this.cpf = data.cpf();
        if (data.rg() != null) this.rg = data.rg();
        if (data.dataNascimento() != null) this.dataNascimento = data.dataNascimento();
        if (data.profissao() != null) this.profissao = data.profissao();
        if (data.estadoCivil() != null) this.estadoCivil = data.estadoCivil();
        if (data.endereco() != null) this.endereco = data.endereco();
        if (data.email() != null) this.email = data.email();
        if (data.checkIn() != null) this.checkIn = data.checkIn();
        if (data.checkOut() != null) this.checkOut = data.checkOut();
        if (data.numMoradores() != null) this.numMoradores = data.numMoradores();
        if (data.valorMensal() != null) this.valorMensal = data.valorMensal();
        if (data.tipoPagamento() != null) this.tipoPagamento = data.tipoPagamento();
        if (data.valorTotal() != null) this.valorTotal = data.valorTotal();
        if (data.percentualComissao() != null) this.percentualComissao = data.percentualComissao();
        if (data.garantia() != null) this.garantia = data.garantia();
        if (data.faxinaIntervaloDias() != null) this.faxinaIntervaloDias = data.faxinaIntervaloDias();
        if (data.ultimaFaxina() != null) this.ultimaFaxina = data.ultimaFaxina();
        if (data.proximaFaxina() != null) this.proximaFaxina = data.proximaFaxina();
        if (data.faxinaStatus() != null) this.faxinaStatus = data.faxinaStatus();
        if (data.faxinaPorMim() != null) this.faxinaPorMim = data.faxinaPorMim();
        if (data.custoEmpresaFaxina() != null) this.custoEmpresaFaxina = data.custoEmpresaFaxina();
        if (data.faxinaPaga() != null) this.faxinaPaga = data.faxinaPaga();
        if (data.faxinaData() != null) this.faxinaData = data.faxinaData();
        if (data.notas() != null) this.notas = data.notas();
        if (data.status() != null) this.status = data.status();
    }
}
