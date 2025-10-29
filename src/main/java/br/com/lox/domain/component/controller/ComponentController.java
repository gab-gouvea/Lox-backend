package br.com.lox.domain.component.controller;

import br.com.lox.domain.component.dto.CreateComponentData;
import br.com.lox.domain.component.dto.UpdateComponentData;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.service.ComponentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/components")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    public ResponseEntity<Component> create(@RequestBody @Valid CreateComponentData data) {
        return componentService.create(data);
    }

    @GetMapping
    public List<Component> findAll() {
        return componentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Component> findById(@PathVariable String id) {
        return componentService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Component> update(@PathVariable String id, @RequestBody @Valid UpdateComponentData data) {
        return  componentService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return componentService.deleteById(id);
    }
}
