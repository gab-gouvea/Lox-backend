package br.com.lox.domain.rental.controller;

import br.com.lox.domain.rental.dto.CreateRentalData;
import br.com.lox.domain.rental.dto.UpdateRentalData;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.service.RentalService;
import jakarta.validation.Valid;
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
        return rentalService.create(data);
    }

    @GetMapping
    public List<Rental> findAll() {
        return rentalService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> findById(@PathVariable String id) {
        return rentalService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rental> update(@PathVariable String id, @RequestBody @Valid UpdateRentalData data) {
        return
    }
}
