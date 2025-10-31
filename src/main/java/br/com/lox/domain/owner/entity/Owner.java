package br.com.lox.domain.owner.entity;

import br.com.lox.domain.owner.dto.UpdateOwnerData;
import br.com.lox.domain.property.entity.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String cpf;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Property> properties = new ArrayList<>();
    private String email;
    private String phone;

    public Owner(String name, String cpf, String email, String phone, List<Property> properties) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.properties = properties;
    }

    public void updateValues(UpdateOwnerData data, List<Property> properties) {
        if (data.name() != null) this.name = data.name();
        if (data.email() != null) this.email = data.email();
        if (data.phone() != null) this.phone = data.phone();
        if (properties != null)  this.properties = properties;
    }
}
