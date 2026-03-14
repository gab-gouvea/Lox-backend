package br.com.lox.domain.inventoryitem.service;

import br.com.lox.domain.inventoryitem.dto.CreateInventoryItemDTO;
import br.com.lox.domain.inventoryitem.dto.UpdateInventoryItemDTO;
import br.com.lox.domain.inventoryitem.entity.InventoryItem;
import br.com.lox.domain.inventoryitem.repository.InventoryItemRepository;
import br.com.lox.exceptions.InventoryItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional
    public InventoryItem create(String propertyId, CreateInventoryItemDTO data) {
        var entity = new InventoryItem(
                propertyId,
                data.comodo(),
                data.nome(),
                data.quantidade(),
                data.descricao(),
                data.imagemUrl()
        );

        return inventoryItemRepository.save(entity);
    }

    public List<InventoryItem> findByPropertyId(String propertyId) {
        return inventoryItemRepository.findByPropriedadeId(propertyId);
    }

    @Transactional
    public InventoryItem update(String id, UpdateInventoryItemDTO data) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Item de inventário não encontrado no sistema: " + id));

        item.updateValues(data);

        return inventoryItemRepository.save(item);
    }

    @Transactional
    public void deleteById(String id) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Item de inventário não encontrado no sistema: " + id));

        inventoryItemRepository.delete(item);
    }
}
