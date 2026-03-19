package br.com.lox.domain.property.entity;

import br.com.lox.domain.property.dto.UpdatePropertyDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "propriedades")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nome;

    private String endereco;

    private String proprietarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType tipo;

    @Column(nullable = false)
    private Integer quartos;

    private String fotoCapa;

    @Column(nullable = false)
    private BigDecimal percentualComissao;

    private BigDecimal taxaLimpeza;

    @Column(nullable = false)
    private Boolean temHobbyBox;

    private String acessoPredio;

    private String acessoApartamento;

    private String senhaWifi;

    @Column(nullable = false)
    private Boolean ativo;

    private Instant inativoAte;

    private String observacaoInatividade;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant atualizadoEm;

    public Property(String nome, String endereco, String proprietarioId, PropertyType tipo,
                    Integer quartos, String fotoCapa, BigDecimal percentualComissao,
                    BigDecimal taxaLimpeza, Boolean temHobbyBox, String acessoPredio,
                    String acessoApartamento, String senhaWifi, Boolean ativo) {
        this.nome = nome;
        this.endereco = endereco;
        this.proprietarioId = proprietarioId;
        this.tipo = tipo;
        this.quartos = quartos;
        this.fotoCapa = fotoCapa;
        this.percentualComissao = percentualComissao;
        this.taxaLimpeza = taxaLimpeza;
        this.temHobbyBox = temHobbyBox;
        this.acessoPredio = acessoPredio;
        this.acessoApartamento = acessoApartamento;
        this.senhaWifi = senhaWifi;
        this.ativo = ativo;
    }

    public void updateValues(UpdatePropertyDTO data) {
        if (data.nome() != null) this.nome = data.nome();
        if (data.endereco() != null) this.endereco = data.endereco();
        if (data.proprietarioId() != null) this.proprietarioId = data.proprietarioId();
        if (data.tipo() != null) this.tipo = data.tipo();
        if (data.quartos() != null) this.quartos = data.quartos();
        if (data.fotoCapa() != null) this.fotoCapa = data.fotoCapa();
        if (data.percentualComissao() != null) this.percentualComissao = data.percentualComissao();
        if (data.taxaLimpeza() != null) this.taxaLimpeza = data.taxaLimpeza();
        if (data.temHobbyBox() != null) this.temHobbyBox = data.temHobbyBox();
        if (data.acessoPredio() != null) this.acessoPredio = data.acessoPredio();
        if (data.acessoApartamento() != null) this.acessoApartamento = data.acessoApartamento();
        if (data.senhaWifi() != null) this.senhaWifi = data.senhaWifi();
        if (data.ativo() != null) this.ativo = data.ativo();
        if (data.inativoAte() != null) this.inativoAte = data.inativoAte();
        if (data.observacaoInatividade() != null) this.observacaoInatividade = data.observacaoInatividade();
        // Allow clearing fields when reactivating
        if (Boolean.TRUE.equals(data.ativo())) {
            this.inativoAte = null;
            this.observacaoInatividade = null;
        }
    }
}
