package br.com.lox.domain.category.service;

import br.com.lox.domain.category.dto.CreateCategoryData;
import br.com.lox.domain.category.dto.UpdateCategoryData;
import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.category.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ResponseEntity<Category> create(@Valid CreateCategoryData data) {
        if (!categoryRepository.existsByName(data.name()) || !categoryRepository.existsByColor(data.color())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var entity = new Category(
                data.name(),
                data.color()
        );

        categoryRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public ResponseEntity<Category> findById(String id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Category> update(String id, @Valid UpdateCategoryData data) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Category category = optionalCategory.get();

        if (!categoryRepository.existsByName(data.name()) || !categoryRepository.existsByColor(data.color())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        category.updateValues(data);
        categoryRepository.save(category);

        return ResponseEntity.ok(category);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Category category = optionalCategory.get();
        categoryRepository.delete(category);

        return ResponseEntity.noContent().build();
    }
}
