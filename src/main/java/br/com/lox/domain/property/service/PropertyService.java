package br.com.lox.domain.property.service;


import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.repository.ComponentRepository;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.repository.OwnerRepository;
import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResponseEntity<Property> create(@Valid CreatePropertyData data) {

        List<Component> components = componentRepository.findAllById(data.componentsId());

        if (components.size() != data.componentsId().size()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!ownerRepository.existsById(data.ownerId())) {
            return ResponseEntity.badRequest().body(null);
        }

        Owner owner = ownerRepository.getReferenceById(data.ownerId());

        var entity = new Property(
                data.title(),
                data.address(),
                data.photos(),
                components,
                owner,
                data.notes(),
                data.senhaPortaria(),
                data.senhaPorta());

        propertyRepository.save(entity);

        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
