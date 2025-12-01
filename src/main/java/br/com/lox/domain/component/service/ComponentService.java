package br.com.lox.domain.component.service;

import br.com.lox.domain.component.dto.CreateComponentData;
import br.com.lox.domain.component.dto.UpdateComponentData;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.repository.ComponentRepository;
import br.com.lox.domain.property.service.PropertyService;
import br.com.lox.exceptions.ComponentNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;

    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Transactional
    public ResponseEntity<Component> create(@Valid CreateComponentData data) {
        if (componentRepository.existsByName(data.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var entity = new Component(
                data.name(),
                data.lastMaintenance(),
                data.nextMaintenance(),
                data.maintenanceDays(),
                data.maintenanceCost(),
                data.notes()
        );

        componentRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    public List<Component> findAllByIds(List<String> ids) {
        List<Component> components = componentRepository.findAllById(ids);

        if (components.size() != ids.size()) {
            throw new ComponentNotFoundException("Um ou mais componentes não foram encontrados: " + ids);
        }

        return components;
    }

    public ResponseEntity<Component> findById(String id) {
        return componentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Component> update(String id, UpdateComponentData data) {
        Optional<Component> optionalComponent = componentRepository.findById(id);

        if (optionalComponent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Component component = optionalComponent.get();
        component.updateValues(data);

        componentRepository.save(component);
        return ResponseEntity.ok(component);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Optional<Component> optionalComponent = componentRepository.findById(id);

        if (optionalComponent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Component component = optionalComponent.get();

        componentRepository.delete(component);
        return ResponseEntity.noContent().build();
    }
}
