package br.com.lox.domain.owner.controller;


import br.com.lox.domain.owner.dto.CreateOwnerData;
import br.com.lox.domain.owner.dto.UpdateOwnerData;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    public ResponseEntity<Owner> save(@RequestBody @Valid CreateOwnerData data) {
        Owner owner = ownerService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(owner);
    }

    @GetMapping
    public ResponseEntity<List<Owner>> findAll() {
       List<Owner> owners = ownerService.findAll();
       return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> findById(@PathVariable String id) {
        Owner owner = ownerService.findById(id);
        return ResponseEntity.ok(owner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable String id, @RequestBody @Valid UpdateOwnerData data) {
        Owner owner = ownerService.update(id, data);
        return ResponseEntity.ok(owner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        ownerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
