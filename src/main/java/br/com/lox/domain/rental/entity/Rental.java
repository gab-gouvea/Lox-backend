package br.com.lox.domain.rental.entity;

import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.property.entity.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    private Integer people;
    private LocalDateTime checkout;
    private LocalDateTime checkin;

    public Rental(String tenantName, Property property, BigDecimal price, Integer people, LocalDateTime checkout, LocalDateTime checkin) {
        this.tenantName = tenantName;
        this.property = property;
        this.price = price;
        this.people = people;
        this.checkout = checkout;
        this.checkin = checkin;
    }
}
