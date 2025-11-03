package br.com.lox.domain.property.entity;


import br.com.lox.domain.checkin.entity.CheckIn;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String address;

    // @OneToOne(cascade = CascadeType.ALL)
    // private CheckIn checkin;

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

    private String conciergeCode; //opcional
    private String doorCode; //opcional


    public Property(String title, String address, Map<Integer, String> photos, List<Component> components, Owner owner, String notes, String c, String c1) {
        this.title = title;
        this.address = address;
        this.photos = photos;
        this.components = components;
        this.owner = owner;
        this.notes = notes;
        this.conciergeCode = c;
        this.doorCode = c1;
    }

    public void updateValues(UpdatePropertyData data, List<Component> components) {
        if (data.title() != null) this.title = data.title();
        if (data.address() != null) this.address = data.address();
        if (data.photos() != null) this.photos = data.photos();
        if (data.notes() != null) this.notes = data.notes();
        if (data.conciergeCode() != null) this.conciergeCode = data.conciergeCode();
        if (data.doorCode() != null) this.doorCode = data.doorCode();
        if (components != null) this.components = components;
    }
}
