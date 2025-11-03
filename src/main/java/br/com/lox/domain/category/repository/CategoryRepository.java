package br.com.lox.domain.category.repository;

import br.com.lox.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
    boolean existsByColor(String color);
}
