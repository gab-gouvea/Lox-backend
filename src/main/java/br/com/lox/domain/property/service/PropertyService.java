package br.com.lox.domain.property.service;

import br.com.lox.domain.property.dto.CreatePropertyDTO;
import br.com.lox.domain.property.dto.UpdatePropertyDTO;
import br.com.lox.domain.property.entity.Property;
import br.com.lox.domain.property.repository.PropertyRepository;
import br.com.lox.exceptions.PropertyNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public Property create(CreatePropertyDTO data) {
        var entity = new Property(
                data.nome(),
                data.endereco(),
                data.proprietarioId(),
                data.tipo(),
                data.quartos(),
                data.fotoCapa(),
                data.percentualComissao(),
                data.taxaLimpeza(),
                data.temHobbyBox(),
                data.acessoPredio(),
                data.acessoApartamento(),
                data.ativo()
        );

        return propertyRepository.save(entity);
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Property findById(String id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + id));
    }

    @Transactional
    public Property update(String id, UpdatePropertyDTO data) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + id));

        property.updateValues(data);

        return propertyRepository.save(property);
    }

    @Transactional
    public void deleteById(String id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Propriedade não encontrada no sistema: " + id));

        propertyRepository.delete(property);
    }
}
