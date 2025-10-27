package br.com.lox.domain.cleaning.entity;

import br.com.lox.domain.rental.entity.Rental;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Cleaning {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    private Rental rental;

    private String tenantName;
    private BigDecimal price;
    private BigDecimal cleaningTax; //opcional
    private LocalDate date;
    private String notes;
}
