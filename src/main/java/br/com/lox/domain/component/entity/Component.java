package br.com.lox.domain.component.entity;


import br.com.lox.domain.component.MaintenanceStatus;
import br.com.lox.domain.component.dto.UpdateComponentData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;
    private Integer maintenanceDays;
    private BigDecimal maintenanceCost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
    private String note;

    public Component(String name, LocalDateTime lastMaintenance, LocalDateTime nextMaintenance, Integer maintenanceDays, BigDecimal maintenanceCost, String notes) {
        this.name = name;
        this.lastMaintenance = lastMaintenance;
        this.nextMaintenance = nextMaintenance;
        this.maintenanceDays = maintenanceDays;
        this.maintenanceCost = maintenanceCost;
        this.note = notes;
    }

    public void updateValues(UpdateComponentData data) {
        if (data.name() != null) this.name = data.name();
        if (data.lastMaintenance() != null) this.lastMaintenance = data.lastMaintenance();
        if (data.nextMaintenance() != null) this.nextMaintenance = data.nextMaintenance();
        if (data.maintenanceDays() != null) this.maintenanceDays = data.maintenanceDays();
        if (data.maintenanceCost() != null) this.maintenanceCost = data.maintenanceCost();
        if (data.notes() != null) this.note = data.notes();
    }
}
