package br.com.lox.domain.item.service;

import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.category.repository.CategoryRepository;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.repository.InventoryRepository;
import br.com.lox.domain.item.dto.CreateItemData;
import br.com.lox.domain.item.dto.UpdateItemData;
import br.com.lox.domain.item.entity.Item;
import br.com.lox.domain.item.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;

    public ItemService(ItemRepository itemRepository, InventoryRepository inventoryRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ResponseEntity<Item> create(CreateItemData data) {
        if (!inventoryRepository.existsById(data.inventoryId())) {
            return ResponseEntity.notFound().build();
        }

        if (!categoryRepository.existsById(data.categoryId())) {
            return ResponseEntity.notFound().build();
        }

        Inventory inventory = inventoryRepository.getReferenceById(data.inventoryId());
        Category category = categoryRepository.getReferenceById(data.categoryId());

        var entity = new Item(
                data.name(),
                data.quantity(),
                data.imageUrl(),
                data.price(),
                inventory,
                category
        );

        itemRepository.save(entity);
        return ResponseEntity.ok(entity);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public ResponseEntity<Item> findById(String id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Item> update(String id, UpdateItemData data) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item item = optionalItem.get();
        Inventory inventory = null;
        Category category = null;

        if (data.inventoryId() != null) {
            Optional<Inventory> optionalInventory = inventoryRepository.findById(data.inventoryId());
            if (optionalInventory.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            inventory = optionalInventory.get();
        }

        if (data.categoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(data.categoryId());
            if (optionalCategory.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            category = optionalCategory.get();
        }

        item.updateValues(category, inventory, data);
        itemRepository.save(item);

        return ResponseEntity.ok(item);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item item = optionalItem.get();
        itemRepository.delete(item);

        return ResponseEntity.noContent().build();
    }
}
