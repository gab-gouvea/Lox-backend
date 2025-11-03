package br.com.lox.domain.inventory.entity;


import br.com.lox.domain.item.entity.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    private LocalDate date;
    private String responsible;


    public Inventory(List<Item> items, LocalDate date, String responsible) {
        this.items = items;
        this.date = date;
        this.responsible = responsible;
    }
}
