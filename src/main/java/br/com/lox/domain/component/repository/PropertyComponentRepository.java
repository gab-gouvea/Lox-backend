package br.com.lox.domain.component.repository;

import br.com.lox.domain.component.entity.PropertyComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyComponentRepository extends JpaRepository<PropertyComponent, String> {
    List<PropertyComponent> findByPropriedadeId(String propriedadeId);
}
