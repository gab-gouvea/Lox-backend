package br.com.lox.domain.inventory.service;

import br.com.lox.domain.inventory.dto.AddItemsData;
import br.com.lox.domain.inventory.dto.CreateInventoryData;
import br.com.lox.domain.inventory.dto.UpdateInventoryData;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.repository.InventoryRepository;
import br.com.lox.domain.item.entity.Item;
import br.com.lox.domain.item.repository.ItemRepository;
import br.com.lox.exceptions.InventoryNotFoundException;
import br.com.lox.exceptions.ItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    public InventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Inventory create(CreateInventoryData data) {
        var entity = new Inventory(
                data.responsible()
        );

        return inventoryRepository.save(entity);
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory findById(String id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventário não encontrado no sistema: " + id));
    }

    @Transactional
    public Inventory update(String id, UpdateInventoryData data) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(("Inventário não encontrado no sistema: " + id)));

        List<Item> items = itemRepository.findAllById(data.itemsId());

        if (items.size() != data.itemsId().size()) {
            throw new ItemNotFoundException("Um ou mais itens não foram encontrados no sistema");
        }

        inventory.newItems(items);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory addItems(String id, AddItemsData data) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventário não encontrado no sistema: " + id));

        List<Item> items = itemRepository.findAllById(data.itemsId());

        if (items.size() != data.itemsId().size()) {
            throw new ItemNotFoundException("Um ou mais itens não foram encontrados no sistema");
        }

        for (Item item : items) {
            inventory.addItem(item);
        }

        return inventoryRepository.save(inventory);
    }


    @Transactional
    public void deleteById(String id) {
        Inventory inventory = inventoryRepository.findById(id)
                        .orElseThrow(() -> new InventoryNotFoundException("Inventário não encontrado no sistema: " + id));

        inventoryRepository.delete(inventory);
    }
}
