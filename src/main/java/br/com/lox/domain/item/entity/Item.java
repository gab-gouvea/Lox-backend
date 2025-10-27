package br.com.lox.domain.item.entity;


import br.com.lox.domain.inventory.entity.Inventory;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    Inventory inventory;

    private String name;
    private int quantity;
    private String ImageUrl;
    private BigDecimal price;


}
