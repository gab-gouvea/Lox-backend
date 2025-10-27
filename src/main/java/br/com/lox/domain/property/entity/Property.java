package br.com.lox.domain.property.entity;


import br.com.lox.domain.checkin.entity.CheckIn;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    private CheckIn checkin;

    @ElementCollection
    @CollectionTable(
            name = "property_photos",
            joinColumns = @JoinColumn(name = "property_id")
    )
    @MapKeyColumn(name = "photo_index")
    @Column(name = "photo_url")
    private Map<Integer , String> photos = new HashMap<>();

    @ManyToMany
    @JoinTable(
            name = "property_components",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "component_id")
    )
    private List<Component> components = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "property")
    private List<Rental> rentals = new ArrayList<>();

    private String notes;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String senhaPortaria; //opcional
    private String senhaPorta; //opcional
}
