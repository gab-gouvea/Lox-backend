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
import br.com.lox.exceptions.PropertyNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PropertyRepository propertyRepository;

    public OwnerService(OwnerRepository ownerRepository, PropertyRepository propertyRepository) {
        this.ownerRepository = ownerRepository;
        this.propertyRepository = propertyRepository;
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
           properties = propertyRepository.findAllById(data.propertiesId());

           if (properties.size() != data.propertiesId().size()) {
               throw new PropertyNotFoundException("Uma ou mais propriedades não foram encontradas no sistema" + data.propertiesId());
           }
        }

        owner.updateValues(data, properties);

        return ownerRepository.save(owner);
    }

    @Transactional
    public void deleteById(String id) {
        Owner owner = ownerRepository.findById(id)
                        .orElseThrow(() -> new OwnerNotFoundException("Proprietário não foi encontrado no sistema " + id));

        ownerRepository.delete(owner);
    }
}
