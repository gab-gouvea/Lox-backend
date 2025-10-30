package br.com.lox.domain.rental.controller;

import br.com.lox.domain.rental.dto.CreateRentalData;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<Rental> create(@RequestBody @Valid CreateRentalData data) {
        return
    }
}
