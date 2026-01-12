package br.com.lox.domain.property.service;


import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.repository.ComponentRepository;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.repository.OwnerRepository;
import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.exceptions.ComponentNotFoundException;
import br.com.lox.exceptions.OwnerNotFoundException;
import br.com.lox.exceptions.PropertyNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Property create(@Valid CreatePropertyData data) {
        List<Component> components = componentRepository.findAllById(data.componentsId());

        if (components.size() != data.componentsId().size()) {
            throw new ComponentNotFoundException("Um ou mais componentes não foram encontrados: " + data.componentsId());
        }

        Owner owner = ownerRepository.findById(data.ownerId())
                .orElseThrow(() -> new OwnerNotFoundException(data.ownerId()));

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
            components = componentRepository.findAllById(data.componentsId());

            if (components.size() != data.componentsId().size()) {
                throw new ComponentNotFoundException("Um ou mais componentes não foram encontrados: " + data.componentsId());
            }
        }

        property.updateValues(data, components);
        return propertyRepository.save(property);
    }

    @Transactional
    public void deleteById(String id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não foi encontrada no sistema " + id));

        propertyRepository.delete(property);
    }
}
