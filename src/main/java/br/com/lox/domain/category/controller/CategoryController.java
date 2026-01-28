package br.com.lox.domain.category.controller;

import br.com.lox.domain.category.dto.CreateCategoryData;
import br.com.lox.domain.category.dto.UpdateCategoryData;
import br.com.lox.domain.category.entity.Category;
import br.com.lox.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        Category category = categoryService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable String id, @RequestBody @Valid UpdateCategoryData data) {
        Category category = categoryService.update(id, data);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
         categoryService.deleteById(id);
         return ResponseEntity.noContent().build();
    }
}
