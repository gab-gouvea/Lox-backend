package br.com.lox.domain.component.controller;

import br.com.lox.domain.component.dto.CreateComponentDTO;
import br.com.lox.domain.component.dto.UpdateComponentDTO;
import br.com.lox.domain.component.entity.PropertyComponent;
import br.com.lox.domain.component.service.PropertyComponentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyComponentController {

    private final PropertyComponentService propertyComponentService;

    public PropertyComponentController(PropertyComponentService propertyComponentService) {
        this.propertyComponentService = propertyComponentService;
    }

    @GetMapping("/api/properties/{propertyId}/components")
    public ResponseEntity<List<PropertyComponent>> findByPropertyId(@PathVariable String propertyId) {
        List<PropertyComponent> components = propertyComponentService.findByPropertyId(propertyId);
        return ResponseEntity.ok(components);
    }

    @PostMapping("/api/properties/{propertyId}/components")
    public ResponseEntity<PropertyComponent> create(@PathVariable String propertyId,
                                                     @RequestBody @Valid CreateComponentDTO data) {
        PropertyComponent component = propertyComponentService.create(propertyId, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(component);
    }

    @GetMapping("/api/components")
    public ResponseEntity<List<PropertyComponent>> findAll() {
        List<PropertyComponent> components = propertyComponentService.findAll();
        return ResponseEntity.ok(components);
    }

    @GetMapping("/api/components/{id}")
    public ResponseEntity<PropertyComponent> findById(@PathVariable String id) {
        PropertyComponent component = propertyComponentService.findById(id);
        return ResponseEntity.ok(component);
    }

    @PutMapping("/api/components/{id}")
    public ResponseEntity<PropertyComponent> update(@PathVariable String id,
                                                     @RequestBody @Valid UpdateComponentDTO data) {
        PropertyComponent component = propertyComponentService.update(id, data);
        return ResponseEntity.ok(component);
    }

    @DeleteMapping("/api/components/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        propertyComponentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
