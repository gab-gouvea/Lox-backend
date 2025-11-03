package br.com.lox.domain.cleaning.entity;

import br.com.lox.domain.cleaning.dto.UpdateCleaningData;
import br.com.lox.domain.rental.entity.Rental;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Cleaning {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    private String tenantName;
    private BigDecimal price;
    private BigDecimal cleaningTax; //opcional
    private LocalDate date;
    private String notes;

    public Cleaning(Rental rental, String tenantName, BigDecimal price, BigDecimal cleaningTax, LocalDate date, String notes) {
        this.rental = rental;
        this.tenantName = tenantName;
        this.price = price;
        this.cleaningTax = cleaningTax;
        this.date = date;
        this.notes = notes;
    }

    public void updateValues(Rental rental, UpdateCleaningData data) {
        if (rental != null) this.rental = rental;
        if (data.tenantName() != null) this.tenantName = data.tenantName();
        if (data.price() != null) this.price = data.price();
        if (data.cleaningTax() != null) this.cleaningTax = data.cleaningTax();
        if (data.date() != null) this.date = data.date();
        if (data.notes() != null) this.notes = data.notes();
    }
}
