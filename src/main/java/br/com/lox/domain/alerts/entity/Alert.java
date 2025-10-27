package br.com.lox.domain.alerts.entity;

import br.com.lox.domain.checkin.entity.CheckIn;
import br.com.lox.domain.cleaning.entity.Cleaning;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.property.entity.Property;
import jakarta.persistence.*;

@Entity
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String message;

    @ManyToOne(optional = true)
    @JoinColumn(name = "component_id")
    private Component component;

    @ManyToOne(optional = true)
    @JoinColumn(name = "cleaning_id")
    private Cleaning cleaning;

    @ManyToOne(optional = true)
    @JoinColumn(name = "checkin_id")
    private CheckIn checkIn;

    @ManyToOne(optional = true)
    @JoinColumn(name = "property_id")
    private Property property;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private AlertType type;

}
