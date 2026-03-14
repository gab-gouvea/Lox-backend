package br.com.lox.domain.inventoryitem.controller;

import br.com.lox.domain.inventoryitem.dto.CreateInventoryItemDTO;
import br.com.lox.domain.inventoryitem.dto.UpdateInventoryItemDTO;
import br.com.lox.domain.inventoryitem.entity.InventoryItem;
import br.com.lox.domain.inventoryitem.service.InventoryItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping("/api/properties/{propertyId}/inventory")
    public ResponseEntity<List<InventoryItem>> findByPropertyId(@PathVariable String propertyId) {
        List<InventoryItem> items = inventoryItemService.findByPropertyId(propertyId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/api/properties/{propertyId}/inventory")
    public ResponseEntity<InventoryItem> create(@PathVariable String propertyId,
                                                 @RequestBody @Valid CreateInventoryItemDTO data) {
        InventoryItem item = inventoryItemService.create(propertyId, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/api/inventory/{id}")
    public ResponseEntity<InventoryItem> update(@PathVariable String id,
                                                 @RequestBody @Valid UpdateInventoryItemDTO data) {
        InventoryItem item = inventoryItemService.update(id, data);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/api/inventory/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        inventoryItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
