package br.com.lox.domain.property.controller;

import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> create(@RequestBody @Valid CreatePropertyData data) {
        Property property = propertyService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }

    @GetMapping
    public ResponseEntity<List<Property>> findAll() {
        List<Property> properties = propertyService.findAll();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> findById(@PathVariable String id) {
        Property property = propertyService.findById(id);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable String id, @RequestBody UpdatePropertyData data) {
       Property property = propertyService.update(id, data);
       return ResponseEntity.ok(property);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        propertyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
