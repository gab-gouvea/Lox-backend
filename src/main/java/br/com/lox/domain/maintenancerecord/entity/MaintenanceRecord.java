package br.com.lox.domain.maintenancerecord.entity;

import br.com.lox.domain.maintenancerecord.dto.UpdateMaintenanceRecordDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "registros_manutencao")
@NoArgsConstructor
@Getter
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;

    @Column(nullable = false)
    private String componenteId;

    @Column(nullable = false)
    private String nomeServico;

    private String prestador;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private Boolean pago;

    public MaintenanceRecord(String propriedadeId, String componenteId, String nomeServico,
                             String prestador, LocalDate data, BigDecimal valor, Boolean pago) {
        this.propriedadeId = propriedadeId;
        this.componenteId = componenteId;
        this.nomeServico = nomeServico;
        this.prestador = prestador;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
    }

    public void updateValues(UpdateMaintenanceRecordDTO data) {
        if (data.propriedadeId() != null) this.propriedadeId = data.propriedadeId();
        if (data.componenteId() != null) this.componenteId = data.componenteId();
        if (data.nomeServico() != null) this.nomeServico = data.nomeServico();
        if (data.prestador() != null) this.prestador = data.prestador();
        if (data.data() != null) this.data = data.data();
        if (data.valor() != null) this.valor = data.valor();
        if (data.pago() != null) this.pago = data.pago();
    }
}
