package br.com.lox.domain.rental.controller;

import br.com.lox.domain.rental.dto.CreateRentalData;
import br.com.lox.domain.rental.dto.UpdateRentalData;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<Rental> create(@RequestBody @Valid CreateRentalData data) {
        Rental rental = rentalService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @GetMapping
    public List<Rental> findAll() {
        return rentalService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> findById(@PathVariable String id) {
        Rental rental =  rentalService.findById(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rental> update(@PathVariable String id, @RequestBody @Valid UpdateRentalData data) {
        Rental rental =  rentalService.update(id, data);
        return ResponseEntity.ok(rental);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        rentalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
