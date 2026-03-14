package br.com.lox.domain.reservation.controller;

import br.com.lox.domain.reservation.dto.CreateReservationDTO;
import br.com.lox.domain.reservation.dto.UpdateReservationDTO;
import br.com.lox.domain.reservation.entity.Reservation;
import br.com.lox.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody @Valid CreateReservationDTO data) {
        Reservation reservation = reservationService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> findAll(
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) Instant start,
            @RequestParam(required = false) Instant end) {
        List<Reservation> reservations = reservationService.findAll(propertyId, start, end);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> findById(@PathVariable String id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable String id, @RequestBody @Valid UpdateReservationDTO data) {
        Reservation reservation = reservationService.update(id, data);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
