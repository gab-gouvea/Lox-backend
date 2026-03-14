package br.com.lox.domain.property.entity;

import br.com.lox.domain.property.dto.UpdatePropertyDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "propriedades")
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

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;

    public Property(String nome, String endereco, String proprietarioId, PropertyType tipo,
                    Integer quartos, String fotoCapa, BigDecimal percentualComissao,
                    BigDecimal taxaLimpeza, Boolean temHobbyBox, String acessoPredio,
                    String acessoApartamento, Boolean ativo) {
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
        this.ativo = ativo;
        this.criadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    public void updateValues(UpdatePropertyDTO data) {
        this.atualizadoEm = Instant.now();
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
        if (data.ativo() != null) this.ativo = data.ativo();
    }
}
