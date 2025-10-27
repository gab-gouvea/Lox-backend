package br.com.lox.domain.component.repository;

import br.com.lox.domain.component.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, String> {
}
