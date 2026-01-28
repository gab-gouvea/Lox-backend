package br.com.lox.domain.inventory.controller;

import br.com.lox.domain.inventory.dto.AddItemsData;
import br.com.lox.domain.inventory.dto.CreateInventoryData;
import br.com.lox.domain.inventory.dto.UpdateInventoryData;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        Inventory inventory = inventoryService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @GetMapping
    public List<Inventory> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable String id) {
       Inventory inventory = inventoryService.findById(id);
       return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable String id, @RequestBody @Valid UpdateInventoryData data) {
        Inventory inventory = inventoryService.update(id, data);
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inventory> addItem(@PathVariable String id, @RequestBody @Valid AddItemsData data) {
        Inventory inventory = inventoryService.addItems(id, data);
        return ResponseEntity.ok(inventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        inventoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
