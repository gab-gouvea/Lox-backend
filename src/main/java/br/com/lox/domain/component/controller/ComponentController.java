package br.com.lox.domain.component.controller;

import br.com.lox.domain.component.dto.CreateComponentData;
import br.com.lox.domain.component.dto.UpdateComponentData;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.service.ComponentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        Component component = componentService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(component);
    }

    @GetMapping
    public List<Component> findAll() {
        return componentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Component> findById(@PathVariable String id) {
        Component component = componentService.findById(id);
        return ResponseEntity.ok(component);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Component> update(@PathVariable String id, @RequestBody @Valid UpdateComponentData data) {
        Component component = componentService.update(id, data);
        return ResponseEntity.ok(component);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
         componentService.deleteById(id);
         return ResponseEntity.noContent().build();
    }
}
