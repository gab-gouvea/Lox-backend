package br.com.lox.domain.inventory.service;

import br.com.lox.domain.inventory.dto.CreateInventoryData;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.repository.InventoryRepository;
import br.com.lox.domain.item.entity.Item;
import br.com.lox.domain.item.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    public InventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<Inventory> create(CreateInventoryData data) {
        List<Item> items = itemRepository.findAllById(data.itemsId());

        if (items.size() != data.itemsId().size()) {
            return ResponseEntity.notFound().build();
        }

        var entity = new Inventory(
                items,
                data.date(),
                data.responsible()
        );

        inventoryRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public ResponseEntity<Inventory> findById(String id) {
        return inventoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    public ResponseEntity<Inventory> update(String id, UpdateInventoryData data) {}

    public ResponseEntity<Void> deleteById(String id) {
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);

        if (optionalInventory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Inventory inventory = optionalInventory.get();
        inventoryRepository.delete(inventory);

        return ResponseEntity.noContent().build();
    }
}
