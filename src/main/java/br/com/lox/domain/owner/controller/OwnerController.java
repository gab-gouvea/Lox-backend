package br.com.lox.domain.owner.controller;


import br.com.lox.domain.owner.dto.CreateOwnerData;
import br.com.lox.domain.owner.dto.UpdateOwnerData;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.service.OwnerService;
import jakarta.validation.Valid;
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
        return ownerService.create(data);
    }

    @GetMapping
    public List<Owner> findAll() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> findById(@PathVariable String id) {
        return ownerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable String id, @RequestBody @Valid UpdateOwnerData data) {
        return ownerService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return  ownerService.deleteById(id);
    }
}
