package br.com.lox.domain.property.controller;

import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> create(@RequestBody @Valid CreatePropertyData data) {
        return propertyService.create(data);
    }

}
