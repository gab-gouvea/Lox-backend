package br.com.lox.domain.locacao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "recebimentos_locacao",
       uniqueConstraints = @UniqueConstraint(columnNames = {"locacao_id", "mes", "ano"}))
@NoArgsConstructor
@Getter
public class RecebimentoLocacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "locacao_id", nullable = false)
    private String locacaoId;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private BigDecimal valorRecebido;

    public RecebimentoLocacao(String locacaoId, Integer mes, Integer ano, BigDecimal valorRecebido) {
        this.locacaoId = locacaoId;
        this.mes = mes;
        this.ano = ano;
        this.valorRecebido = valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }
}
