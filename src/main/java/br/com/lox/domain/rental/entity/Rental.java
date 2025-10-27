package br.com.lox.domain.rental.entity;

import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.property.entity.Property;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String tenantName;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL)
    private Inventory inventory;

    private int people;
    private LocalDateTime checkout;
    private LocalDateTime checkin;
}
