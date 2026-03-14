package br.com.lox.domain.inventoryitem.repository;

import br.com.lox.domain.inventoryitem.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
    List<InventoryItem> findByPropriedadeId(String propriedadeId);
}
