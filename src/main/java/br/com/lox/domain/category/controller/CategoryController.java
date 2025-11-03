package br.com.lox.domain.category.controller;

import br.com.lox.domain.category.dto.CreateCategoryData;
import br.com.lox.domain.category.dto.UpdateCategoryData;
import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid CreateCategoryData data) {
        return categoryService.create(data);
    }

    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable String id, @RequestBody @Valid UpdateCategoryData data) {
        return categoryService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return categoryService.deleteById(id);
    }
}
