package br.com.lox.domain.item.service;

import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.category.repository.CategoryRepository;
import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.repository.InventoryRepository;
import br.com.lox.domain.item.dto.CreateItemData;
import br.com.lox.domain.item.dto.UpdateItemData;
import br.com.lox.domain.item.entity.Item;
import br.com.lox.domain.item.repository.ItemRepository;
import br.com.lox.exceptions.CategoryNotFoundException;
import br.com.lox.exceptions.InventoryNotFoundException;
import br.com.lox.exceptions.ItemNotFoundException;
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
    public Item create(CreateItemData data) {
        Inventory inventory = inventoryRepository.findById(data.inventoryId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventário não encontrado no sistema " + data.inventoryId()));

        Category category = categoryRepository.findById(data.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada no sistema " + data.categoryId()));

        var entity = new Item(
                data.name(),
                data.quantity(),
                data.imageUrl(),
                data.price(),
                inventory,
                category
        );

        return itemRepository.save(entity);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item não encontrado no sistema: " + id));
    }

    @Transactional
    public Item update(String id, UpdateItemData data) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item não encontrado no sistema: " + id));


        Inventory inventory = null;
        Category category = null;
        if (data.inventoryId() != null) {
            inventory = inventoryRepository.findById(data.inventoryId())
                    .orElseThrow(() -> new InventoryNotFoundException("Inventário não encontrado no sistema: " + data.inventoryId()));
        }

        if (data.categoryId() != null) {
            category = categoryRepository.findById(data.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada no sistema: " + data.categoryId()));
        }

        item.updateValues(category, inventory, data);
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteById(String id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item não encontrado no sistema: " + id));

        itemRepository.delete(item);
    }
}
