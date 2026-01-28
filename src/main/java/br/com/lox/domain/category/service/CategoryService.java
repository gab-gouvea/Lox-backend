package br.com.lox.domain.category.service;

import br.com.lox.domain.category.dto.CreateCategoryData;
import br.com.lox.domain.category.dto.UpdateCategoryData;
import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.category.repository.CategoryRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.CategoryNotFoundException;
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
    public Category create(@Valid CreateCategoryData data) {
        if (categoryRepository.existsByName(data.name()) || categoryRepository.existsByColor(data.color())) {
            throw new BusinessRuleException("Nome ou cor já de categoria já existem no sistema.");
        }

        var entity = new Category(
                data.name(),
                data.color()
        );

        return categoryRepository.save(entity);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada no sistema: " + id));
    }

    @Transactional
    public Category update(String id, @Valid UpdateCategoryData data) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada no sistema: " + id));

        if (!categoryRepository.existsByName(data.name()) || !categoryRepository.existsByColor(data.color())) {
            throw new BusinessRuleException("Nome ou cor já de categoria já existem no sistema.");
        }

        category.updateValues(data);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada no sistema: " + id));

        categoryRepository.delete(category);
    }
}
