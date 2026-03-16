package br.com.lox.domain.scheduledmaintenance.entity;

import br.com.lox.domain.scheduledmaintenance.dto.UpdateScheduledMaintenanceDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "manutencoes_agendadas")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class ScheduledMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataPrevista;

    private String prestador;

    @Column(nullable = false)
    private Boolean confirmada = false;

    private BigDecimal valor;

    private LocalDate dataConclusao;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    public ScheduledMaintenance(String propriedadeId, String nome, LocalDate dataPrevista, String prestador) {
        this.propriedadeId = propriedadeId;
        this.nome = nome;
        this.dataPrevista = dataPrevista;
        this.prestador = prestador;
        this.confirmada = false;
    }

    public void updateValues(UpdateScheduledMaintenanceDTO data) {
        if (data.nome() != null) this.nome = data.nome();
        if (data.dataPrevista() != null) this.dataPrevista = data.dataPrevista();
        if (data.prestador() != null) this.prestador = data.prestador();
    }

    public void confirmar(BigDecimal valor, String prestador) {
        this.confirmada = true;
        this.valor = valor;
        this.dataConclusao = LocalDate.now();
        if (prestador != null) this.prestador = prestador;
    }
}
