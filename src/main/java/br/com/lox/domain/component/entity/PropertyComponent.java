package br.com.lox.domain.component.entity;

import br.com.lox.domain.component.dto.UpdateComponentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "componentes")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class PropertyComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate ultimaManutencao;

    @Column(nullable = false)
    private LocalDate proximaManutencao;

    @Column(nullable = false)
    private Integer intervaloDias;

    private String prestador;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant atualizadoEm;

    public PropertyComponent(String propriedadeId, String nome, LocalDate ultimaManutencao,
                             LocalDate proximaManutencao, Integer intervaloDias,
                             String prestador, String observacoes) {
        this.propriedadeId = propriedadeId;
        this.nome = nome;
        this.ultimaManutencao = ultimaManutencao;
        this.proximaManutencao = proximaManutencao;
        this.intervaloDias = intervaloDias;
        this.prestador = prestador;
        this.observacoes = observacoes;
    }

    public void updateValues(UpdateComponentDTO data) {
        if (data.nome() != null) this.nome = data.nome();
        if (data.ultimaManutencao() != null) this.ultimaManutencao = data.ultimaManutencao();
        if (data.proximaManutencao() != null) this.proximaManutencao = data.proximaManutencao();
        if (data.intervaloDias() != null) this.intervaloDias = data.intervaloDias();
        if (data.prestador() != null) this.prestador = data.prestador();
        if (data.observacoes() != null) this.observacoes = data.observacoes();
    }
}
