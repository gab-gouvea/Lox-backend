package br.com.lox.domain.item.controller;

import br.com.lox.domain.item.dto.CreateItemData;
import br.com.lox.domain.item.dto.UpdateItemData;
import br.com.lox.domain.item.entity.Item;
import br.com.lox.domain.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        Item item = itemService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping
    public List<Item> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable String id) {
        Item item =  itemService.findById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable String id, @RequestBody UpdateItemData data) {
        Item item = itemService.update(id, data);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
      itemService.deleteById(id);
      return ResponseEntity.noContent().build();
    }
}
