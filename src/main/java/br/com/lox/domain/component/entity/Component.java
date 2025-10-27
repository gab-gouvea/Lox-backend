package br.com.lox.domain.component.entity;


import br.com.lox.domain.component.MaintenanceStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;
    private int maintenanceDays;
    private BigDecimal maintenanceCost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
    private String note;

}
