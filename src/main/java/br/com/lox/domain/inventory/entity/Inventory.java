package br.com.lox.domain.inventory.entity;


import br.com.lox.domain.item.entity.Item;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Item> items = new ArrayList<>();

    @CreatedDate
    private LocalDate date;

    @LastModifiedDate
    private LocalDate lastUpdate;

    private String responsible;

    public Inventory(String responsible) {
        this.responsible = responsible;
    }

    public void newItems(List<Item> items) {
        this.items.clear();

        for (Item item : items) {
            addItem(item);
        }
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setInventory(this);
    }

}
