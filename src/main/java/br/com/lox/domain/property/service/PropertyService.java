package br.com.lox.domain.property.service;


import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.repository.ComponentRepository;
import br.com.lox.domain.component.service.ComponentService;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.repository.OwnerRepository;
import br.com.lox.domain.owner.service.OwnerService;
import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.exceptions.ComponentNotFoundException;
import br.com.lox.exceptions.PropertyNotFoundException;
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
    private final ComponentService componentService;
    private final OwnerService ownerService;

    public PropertyService(PropertyRepository propertyRepository, ComponentRepository componentRepository, OwnerRepository ownerRepository, ComponentService componentService, OwnerService ownerService) {
        this.propertyRepository = propertyRepository;
        this.componentService = componentService;
        this.ownerService = ownerService;
    }

    @Transactional
    public Property create(@Valid CreatePropertyData data) {
        List<Component> components = componentService.findAllByIds(data.componentsId());

        Owner owner = ownerService.findById(data.ownerId());

        var entity = new Property(
                data.title(),
                data.address(),
                data.photos(),
                components,
                owner,
                data.notes(),
                data.conciergeCode(),
                data.doorCode()
        );

        return propertyRepository.save(entity);
    }

    //@Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public List<Property> findAllByIds(List<String> ids) {
        List<Property> properties = propertyRepository.findAllById(ids);

        if (properties.size() != ids.size()) {
            throw new PropertyNotFoundException("Um ou mais propriedades não foram encontradas: " + ids);
        }

        return properties;
    }

    //@Transactional(readOnly = true)
    public Property findById(String id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não foi encontrada no sistema " + id));
    }

    @Transactional
    public Property update(String id ,UpdatePropertyData data) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não foi encontrada no sistema " + id));

        List<Component> components = null;
        if (data.componentsId() != null) {
            components = componentService.findAllByIds(data.componentsId());
        }

        property.updateValues(data, components);
        return propertyRepository.save(property);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não foi encontrada no sistema " + id));

        propertyRepository.delete(property);
    }
}
