package br.com.lox.domain.locacao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Parcela da taxa de intermediação de uma locação sem administração.
 * A taxa total pode ser recebida de uma vez ou dividida em vários meses
 * (ex.: metade em outubro, metade em novembro).
 * O dia é informativo — relatórios e confirmação de recebimento trabalham por mês/ano.
 */
@Entity
@Table(name = "parcelas_taxa")
@NoArgsConstructor
@Getter
public class ParcelaTaxa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer dia;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private BigDecimal valor;

    public ParcelaTaxa(Integer dia, Integer mes, Integer ano, BigDecimal valor) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.valor = valor;
    }
}
