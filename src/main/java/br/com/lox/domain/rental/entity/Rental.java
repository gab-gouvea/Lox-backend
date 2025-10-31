package br.com.lox.domain.rental.entity;

import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.rental.dto.UpdateRentalData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
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

    public void updateValues(Inventory inventory, Property property, UpdateRentalData data) {
        if (inventory != null) this.inventory = inventory;
        if (property != null) this.property = property;
        if (data.tenantName() != null) this.tenantName = data.tenantName();
        if (data.price() != null) this.price = data.price();
        if (data.people() != null) this.people = data.people();
        if (data.checkout() != null) this.checkout = data.checkout();
        if (data.checkin() != null) this.checkin = data.checkin();
    }
}
