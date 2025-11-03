package br.com.lox.domain.category.entity;


import br.com.lox.domain.category.dto.UpdateCategoryData;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String color;

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void updateValues(UpdateCategoryData data) {
        if (data.name() != null) this.name = data.name();
        if (data.color() != null) this.color = data.color();
    }
}
