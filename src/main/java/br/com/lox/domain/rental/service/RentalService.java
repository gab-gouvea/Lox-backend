package br.com.lox.domain.rental.service;

import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.repository.InventoryRepository;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.rental.dto.CreateRentalData;
import br.com.lox.domain.rental.dto.UpdateRentalData;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.repository.RentalRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.InventoryNotFoundException;
import br.com.lox.exceptions.PropertyNotFoundException;
import br.com.lox.exceptions.RentalNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final PropertyRepository propertyRepository;
    private final InventoryRepository inventoryRepository;

    public RentalService(RentalRepository rentalRepository, PropertyRepository propertyRepository, InventoryRepository inventoryRepository) {
        this.rentalRepository = rentalRepository;
        this.propertyRepository = propertyRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Rental create(@Valid CreateRentalData data) {
        Property property = propertyRepository.findById(data.propertyId())
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema."));

        var entity = new Rental(
                data.tenantName(),
                property,
                data.price(),
                data.people(),
                data.checkout(),
                data.checkin()
        );

        return rentalRepository.save(entity);
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public Rental findById(String id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Reserva não encontrada no sistema." + id));
    }

    @Transactional
    public Rental update(String id, UpdateRentalData data) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Reserva não encontrada no sistema." + id));

        Inventory inventory = null;
        Property property = null;
        if (data.inventoryId() != null) {
            inventory = inventoryRepository.findById(data.inventoryId())
                    .orElseThrow(() -> new InventoryNotFoundException("Inventário não encontrado no sistema." + data.inventoryId()));
        }

        if (data.propertyId() != null) {
           property = propertyRepository.findById(data.propertyId())
                   .orElseThrow(() -> new PropertyNotFoundException("Propriedade não foi encontrada no sistema." + data.propertyId()));
        }

        rental.updateValues(inventory, property, data);
        return rentalRepository.save(rental);
    }

    @Transactional
    public void deleteById(String id) {
        Rental rental = rentalRepository.findById(id)
                        .orElseThrow(() -> new RentalNotFoundException("Reserva não encontrada no sistema." + id));

        rentalRepository.delete(rental);
    }
}
