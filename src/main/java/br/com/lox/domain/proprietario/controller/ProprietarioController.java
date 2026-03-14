package br.com.lox.domain.proprietario.controller;

import br.com.lox.domain.proprietario.dto.CreateProprietarioDTO;
import br.com.lox.domain.proprietario.dto.UpdateProprietarioDTO;
import br.com.lox.domain.proprietario.entity.Proprietario;
import br.com.lox.domain.proprietario.service.ProprietarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proprietarios")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    @PostMapping
    public ResponseEntity<Proprietario> create(@RequestBody @Valid CreateProprietarioDTO data) {
        Proprietario proprietario = proprietarioService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(proprietario);
    }

    @GetMapping
    public ResponseEntity<List<Proprietario>> findAll() {
        List<Proprietario> proprietarios = proprietarioService.findAll();
        return ResponseEntity.ok(proprietarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietario> findById(@PathVariable String id) {
        Proprietario proprietario = proprietarioService.findById(id);
        return ResponseEntity.ok(proprietario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proprietario> update(@PathVariable String id, @RequestBody @Valid UpdateProprietarioDTO data) {
        Proprietario proprietario = proprietarioService.update(id, data);
        return ResponseEntity.ok(proprietario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        proprietarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
