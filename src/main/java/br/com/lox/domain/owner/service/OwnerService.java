package br.com.lox.domain.owner.service;

import br.com.lox.domain.owner.dto.CreateOwnerData;
import br.com.lox.domain.owner.dto.UpdateOwnerData;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.repository.OwnerRepository;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PropertyRepository propertyRepository;

    public OwnerService(OwnerRepository ownerRepository, PropertyRepository propertyRepository) {
        this.ownerRepository = ownerRepository;
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public ResponseEntity<Owner> create(CreateOwnerData data) {
        if (ownerRepository.existsByCpf(data.cpf()) || ownerRepository.existsByEmail(data.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        List<Property> properties = propertyRepository.findAllById(data.propertiesId());
        if (properties.size() != data.propertiesId().size()) {
            return ResponseEntity.notFound().build();
        }

        var entity = new Owner(
                data.name(),
                data.cpf(),
                data.email(),
                data.phone(),
                properties
        );

        ownerRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    public ResponseEntity<Owner> findById(String id) {
        return ownerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Owner> update(String id, UpdateOwnerData data) {
        Optional<Owner> optionalOwner = ownerRepository.findById(id);

        if (optionalOwner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Owner owner = optionalOwner.get();

        List<Property> properties = null;
        if (data.propertiesId() != null) {
            properties = propertyRepository.findAllById(data.propertiesId());
            if (properties.size() != data.propertiesId().size()) {
                return ResponseEntity.notFound().build();
            }
        }

        owner.updateValues(data, properties);
        ownerRepository.save(owner);

        return ResponseEntity.ok(owner);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(String id) {
        Optional<Owner> optionalOwner = ownerRepository.findById(id);

        if (optionalOwner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Owner owner = optionalOwner.get();
        ownerRepository.delete(owner);

        return ResponseEntity.noContent().build();
    }
}
