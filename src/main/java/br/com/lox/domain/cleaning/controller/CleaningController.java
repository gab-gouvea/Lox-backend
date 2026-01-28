package br.com.lox.domain.cleaning.controller;

import br.com.lox.domain.cleaning.dto.CreateCleaningData;
import br.com.lox.domain.cleaning.dto.UpdateCleaningData;
import br.com.lox.domain.cleaning.entity.Cleaning;
import br.com.lox.domain.cleaning.service.CleaningService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        Cleaning cleaning =  cleaningService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(cleaning);
    }

    @GetMapping
    public List<Cleaning> findAll() {
        return cleaningService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cleaning> findById(@PathVariable String id) {
        Cleaning cleaning = cleaningService.findById(id);
        return ResponseEntity.ok().body(cleaning);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cleaning> update(@PathVariable String id, @RequestBody UpdateCleaningData data) {
        Cleaning cleaning = cleaningService.update(id, data);
        return ResponseEntity.ok().body(cleaning);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        cleaningService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
