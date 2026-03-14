package br.com.lox.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "despesas")
@NoArgsConstructor
@Getter
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private Boolean reembolsavel;

    public Despesa(String descricao, BigDecimal valor, Boolean reembolsavel) {
        this.descricao = descricao;
        this.valor = valor;
        this.reembolsavel = reembolsavel;
    }
}
