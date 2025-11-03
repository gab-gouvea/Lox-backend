package br.com.lox.domain.cleaning.controller;

import br.com.lox.domain.cleaning.dto.CreateCleaningData;
import br.com.lox.domain.cleaning.dto.UpdateCleaningData;
import br.com.lox.domain.cleaning.entity.Cleaning;
import br.com.lox.domain.cleaning.service.CleaningService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cleaning")
public class CleaningController {

    private final CleaningService cleaningService;

    public CleaningController(CleaningService cleaningService) {
        this.cleaningService = cleaningService;
    }

    @PostMapping
    public ResponseEntity<Cleaning> create(@RequestBody @Valid CreateCleaningData data) {
        return cleaningService.create(data);
    }

    @GetMapping
    public List<Cleaning> findAll() {
        return cleaningService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cleaning> findById(@PathVariable String id) {
        return cleaningService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cleaning> update(@PathVariable String id, @RequestBody UpdateCleaningData data) {
        return cleaningService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return cleaningService.deleteById(id);
    }
}
