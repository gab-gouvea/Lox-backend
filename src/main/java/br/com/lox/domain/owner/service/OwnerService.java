package br.com.lox.domain.owner.service;

import br.com.lox.domain.owner.dto.CreateOwnerData;
import br.com.lox.domain.owner.dto.UpdateOwnerData;
import br.com.lox.domain.owner.entity.Owner;
import br.com.lox.domain.owner.repository.OwnerRepository;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.domain.property.service.PropertyService;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.OwnerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PropertyService propertyService;

    public OwnerService(OwnerRepository ownerRepository, PropertyRepository propertyRepository, PropertyService propertyService) {
        this.ownerRepository = ownerRepository;
        this.propertyService = propertyService;
    }

    @Transactional
    public Owner create(CreateOwnerData data) {
        if (ownerRepository.existsByCpf(data.cpf()) || ownerRepository.existsByEmail(data.email())) {
            throw new BusinessRuleException("Proprietário já cadastrado");
        }

        var entity = new Owner(
                data.name(),
                data.cpf(),
                data.email(),
                data.phone()
        );

        return ownerRepository.save(entity);
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    public Owner findById(String id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Proprietário não foi encontrado no sistema " + id));
    }

    @Transactional
    public Owner update(String id, UpdateOwnerData data) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Proprietário não foi encontrado no sistema " + id));

        List<Property> properties = null;
        if (data.propertiesId() != null) {
           properties = propertyService.findAllByIds(data.propertiesId());
        }

        owner.updateValues(data, properties);

        return ownerRepository.save(owner);
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
