package br.com.lox.domain.owner.entity;

import br.com.lox.domain.property.entity.Property;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
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
}
