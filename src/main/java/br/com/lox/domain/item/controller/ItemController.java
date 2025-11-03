package br.com.lox.domain.item.controller;

import br.com.lox.domain.item.dto.CreateItemData;
import br.com.lox.domain.item.dto.UpdateItemData;
import br.com.lox.domain.item.entity.Item;
import br.com.lox.domain.item.service.ItemService;
import br.com.lox.domain.property.dto.CreatePropertyData;
import br.com.lox.domain.property.dto.UpdatePropertyData;
import br.com.lox.domain.property.entity.Property;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody @Valid CreateItemData data) {
        return itemService.create(data);
    }

    @GetMapping
    public List<Item> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable String id) {
        return itemService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable String id, @RequestBody UpdateItemData data) {
        return itemService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return itemService.deleteById(id);
    }
}
