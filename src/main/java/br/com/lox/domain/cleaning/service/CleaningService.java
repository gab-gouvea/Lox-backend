package br.com.lox.domain.cleaning.service;

import br.com.lox.domain.cleaning.dto.CreateCleaningData;
import br.com.lox.domain.cleaning.dto.UpdateCleaningData;
import br.com.lox.domain.cleaning.entity.Cleaning;
import br.com.lox.domain.cleaning.repository.CleaningRepository;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.repository.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CleaningService {

    private final CleaningRepository cleaningRepository;
    private final RentalRepository rentalRepository;

    public CleaningService(CleaningRepository cleaningRepository, RentalRepository rentalRepository) {
        this.cleaningRepository = cleaningRepository;
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    public ResponseEntity<Cleaning> create(CreateCleaningData data) {
        if (!rentalRepository.existsById(data.rentalId())) {
            return ResponseEntity.notFound().build();
        }

        Rental rental = rentalRepository.getReferenceById(data.rentalId());

        var entity = new Cleaning(
                rental,
                data.tenantName(),
                data.price(),
                data.cleaningTax(),
                data.date(),
                data.notes()
        );

        cleaningRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    public List<Cleaning> findAll() {
        return cleaningRepository.findAll();
    }

    public ResponseEntity<Cleaning> findById(String id) {
        return cleaningRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Cleaning> update(String id, UpdateCleaningData data) {
        Optional<Cleaning> optionalCleaning = cleaningRepository.findById(id);

        if (optionalCleaning.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cleaning cleaning = optionalCleaning.get();
        Rental rental = null;

        if (data.rentalId() != null) {
            Optional<Rental> optionalRental = rentalRepository.findById(data.rentalId());
            if (optionalRental.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            rental = optionalRental.get();
        }

        cleaning.updateValues(rental, data);
        cleaningRepository.save(cleaning);

        return ResponseEntity.ok(cleaning);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Optional<Cleaning> optionalCleaning = cleaningRepository.findById(id);

        if (optionalCleaning.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cleaning cleaning = optionalCleaning.get();
        cleaningRepository.delete(cleaning);

        return ResponseEntity.noContent().build();
    }
}
