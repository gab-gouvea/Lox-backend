package br.com.lox.domain.inventory.entity;


import br.com.lox.domain.item.entity.Item;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    private LocalDate date;
    private String responsible;




}
