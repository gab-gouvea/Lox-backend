package br.com.lox.domain.property.controller;

import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.service.PropertyService;
import jakarta.validation.Valid;
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
        return propertyService.create(data);
    }

    @GetMapping
    public List<Property> findAll() {
        return propertyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> findById(@PathVariable String id) {
        return propertyService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable String id, @RequestBody UpdatePropertyData data) {
        return propertyService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return propertyService.deleteById(id);
    }
}
