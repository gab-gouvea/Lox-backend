package br.com.lox.domain.property.service;


import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.repository.ComponentRepository;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.repository.OwnerRepository;
import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final ComponentRepository componentRepository;
    private final OwnerRepository ownerRepository;

    public PropertyService(PropertyRepository propertyRepository, ComponentRepository componentRepository, OwnerRepository ownerRepository) {
        this.propertyRepository = propertyRepository;
        this.componentRepository = componentRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public ResponseEntity<Property> create(@Valid CreatePropertyData data) {

        List<Component> components = componentRepository.findAllById(data.componentsId());

        if (components.size() != data.componentsId().size()) {
            return ResponseEntity.notFound().build();
        }

        // Owner owner = ownerService.findById(data.ownerId()));
        if (!ownerRepository.existsById(data.ownerId())) {
            return ResponseEntity.notFound().build();
        }

        Owner owner = ownerRepository.getReferenceById(data.ownerId());

        var entity = new Property(
                data.title(),
                data.address(),
                data.photos(),
                components,
                owner,
                data.notes(),
                data.conciergeCode(),
                data.doorCode());

        propertyRepository.save(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    //@Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    //@Transactional(readOnly = true)
    public ResponseEntity<Property> findById(String id) {
        return propertyRepository.findById(id)
                .map(ResponseEntity::ok) // .map(p -> ResponseEntity.ok(p))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Property> update(String id ,UpdatePropertyData data) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);

        if (optionalProperty.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property property = optionalProperty.get();

        List<Component> components = null;
        if (data.componentsId() != null) {
            components = componentRepository.findAllById(data.componentsId());
            if (components.size() != data.componentsId().size()) {
                return ResponseEntity.notFound().build();
            }
        }

        property.updateValues(data, components);
        propertyRepository.save(property);

        return ResponseEntity.ok(property);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);

        if (optionalProperty.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property property = optionalProperty.get();
        propertyRepository.delete(property);
        return ResponseEntity.noContent().build();
    }

}
