package br.com.lox.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "extensoes")
@NoArgsConstructor
@Getter
public class Extensao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Instant dataInicio;

    @Column(nullable = false)
    private BigDecimal valor;

    /**
     * Cada extensão é paga no mês em que começa, separada da reserva base — por isso tem
     * confirmação de recebimento própria, e não a da reserva.
     */
    private Boolean pagamentoRecebido;

    public Extensao(Instant dataInicio, BigDecimal valor) {
        this.dataInicio = dataInicio;
        this.valor = valor;
    }

    public Extensao(Instant dataInicio, BigDecimal valor, Boolean pagamentoRecebido) {
        this.dataInicio = dataInicio;
        this.valor = valor;
        this.pagamentoRecebido = pagamentoRecebido;
    }
}
