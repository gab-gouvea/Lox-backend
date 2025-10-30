package br.com.lox.domain.rental.service;

import br.com.lox.domain.inventory.entity.Inventory;
import br.com.lox.domain.inventory.repository.InventoryRepository;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.rental.dto.CreateRentalData;
import br.com.lox.domain.rental.dto.UpdateRentalData;
import br.com.lox.domain.rental.entity.Rental;
import br.com.lox.domain.rental.repository.RentalRepository;
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
    public ResponseEntity<Rental> create(@Valid CreateRentalData data) {
        if (!propertyRepository.existsById(data.propertyId())) {
            return ResponseEntity.notFound().build();
        }

        Property property = propertyRepository.getReferenceById(data.propertyId());

        var entity = new Rental(
                data.tenantRental(),
                property,
                data.price(),
                data.people(),
                data.checkout(),
                data.checkin()
        );

        rentalRepository.save(entity);
        return ResponseEntity.ok(entity);
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public ResponseEntity<Rental> findById(String id) {
        return rentalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Rental> update(String id, UpdateRentalData data) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);

        if (optionalRental.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Rental rental = optionalRental.get();

        Inventory inventory = null;
        Property property = null;

        if (data.inventoryId() != null) {
            Optional<Inventory> optionalInventory = inventoryRepository.findById(data.inventoryId());
            if (optionalInventory.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            inventory = optionalInventory.get();
        }

        if (data.propertyId() != null) {
            Optional<Property> optionalProperty = propertyRepository.findById(data.propertyId());
            if (optionalProperty.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            property = optionalProperty.get();
        }









    }



}
