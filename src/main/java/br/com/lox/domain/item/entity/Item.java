package br.com.lox.domain.item.entity;


import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.item.dto.UpdateItemData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    @JsonBackReference
    Inventory inventory;

    private String name;
    private Integer quantity;
    private String ImageUrl;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Item(String name, Integer quantity, String imageUrl, BigDecimal price, Inventory inventory, Category category) {
        this.name = name;
        this.quantity = quantity;
        this.ImageUrl = imageUrl;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }

    public void updateValues(Category category, Inventory inventory, UpdateItemData data) {
        if (category != null) this.category = category;
        if (inventory != null) this.inventory = inventory;
        if (data.name() != null) this.name = data.name();
        if (data.quantity() != null) this.quantity = data.quantity();
        if (data.imageUrl() != null) this.ImageUrl = data.imageUrl();
        if (data.price() != null) this.price = data.price();
    }
}
