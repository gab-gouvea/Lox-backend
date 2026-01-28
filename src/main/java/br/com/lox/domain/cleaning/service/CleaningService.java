package br.com.lox.domain.cleaning.service;

import br.com.lox.domain.cleaning.dto.CreateCleaningData;
import br.com.lox.domain.cleaning.dto.UpdateCleaningData;
import br.com.lox.domain.cleaning.entity.Cleaning;
import br.com.lox.domain.cleaning.repository.CleaningRepository;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.repository.RentalRepository;
import br.com.lox.exceptions.CleaningNotFoundException;
import br.com.lox.exceptions.RentalNotFoundException;
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
    public Cleaning create(CreateCleaningData data) {
        Rental rental = rentalRepository.findById(data.rentalId())
                .orElseThrow(() -> new RentalNotFoundException("Reserva não encontrada no sistema: " + data.rentalId()));

        var entity = new Cleaning(
                rental,
                data.tenantName(),
                data.price(),
                data.cleaningTax(),
                data.date(),
                data.notes()
        );

        return  cleaningRepository.save(entity);

    }

    public List<Cleaning> findAll() {
        return cleaningRepository.findAll();
    }

    public Cleaning findById(String id) {
        return cleaningRepository.findById(id)
                .orElseThrow(() -> new CleaningNotFoundException("Faxina não encontrada no sistema: " + id));
    }

    @Transactional
    public Cleaning update(String id, UpdateCleaningData data) {
        Cleaning cleaning = cleaningRepository.findById(id)
                .orElseThrow(() -> new CleaningNotFoundException("Faxina não encontrada no sistema: " + id));

        Rental rental = null;
        if (data.rentalId() != null) {
            rental = rentalRepository.findById(data.rentalId())
                    .orElseThrow(() -> new CleaningNotFoundException("Faxina não encontrada no sistema: " + data.rentalId()));
            }

        cleaning.updateValues(rental, data);
        return cleaningRepository.save(cleaning);
    }

    @Transactional
    public void deleteById(String id) {
        Cleaning cleaning = cleaningRepository.findById(id)
                        .orElseThrow(() -> new CleaningNotFoundException("Faxina não encontrada no sistema: " + id));

        cleaningRepository.delete(cleaning);
    }
}
