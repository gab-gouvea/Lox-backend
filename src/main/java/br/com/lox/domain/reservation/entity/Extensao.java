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

    public Extensao(Instant dataInicio, BigDecimal valor) {
        this.dataInicio = dataInicio;
        this.valor = valor;
    }
}
