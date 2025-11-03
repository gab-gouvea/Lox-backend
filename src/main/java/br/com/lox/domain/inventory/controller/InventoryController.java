package br.com.lox.domain.inventory.controller;

import br.com.lox.domain.inventory.dto.CreateInventoryData;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody @Valid CreateInventoryData data) {
        return inventoryService.create(data);
    }

    @GetMapping
    public List<Inventory> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable String id) {
        return inventoryService.findById(id);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Inventory> update(@PathVariable String id, @RequestBody UpdateInventoryData data) {
//        return inventoryService.update(id, data);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return inventoryService.deleteById(id);
    }
}
