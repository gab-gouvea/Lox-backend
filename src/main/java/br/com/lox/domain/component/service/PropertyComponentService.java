package br.com.lox.domain.component.service;

import br.com.lox.domain.component.dto.CreateComponentDTO;
import br.com.lox.domain.component.dto.UpdateComponentDTO;
import br.com.lox.domain.component.entity.PropertyComponent;
import br.com.lox.domain.component.repository.PropertyComponentRepository;
import br.com.lox.exceptions.ComponentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PropertyComponentService {

    private final PropertyComponentRepository propertyComponentRepository;

    public PropertyComponentService(PropertyComponentRepository propertyComponentRepository) {
        this.propertyComponentRepository = propertyComponentRepository;
    }

    @Transactional
    public PropertyComponent create(String propertyId, CreateComponentDTO data) {
        LocalDate proximaManutencao = data.proximaManutencao();
        if (proximaManutencao == null) {
            proximaManutencao = data.ultimaManutencao().plusDays(data.intervaloDias());
        }

        var entity = new PropertyComponent(
                propertyId,
                data.nome(),
                data.ultimaManutencao(),
                proximaManutencao,
                data.intervaloDias(),
                data.prestador(),
                data.observacoes()
        );

        return propertyComponentRepository.save(entity);
    }

    public List<PropertyComponent> findAll() {
        return propertyComponentRepository.findAll();
    }

    public List<PropertyComponent> findByPropertyId(String propertyId) {
        return propertyComponentRepository.findByPropriedadeId(propertyId);
    }

    public PropertyComponent findById(String id) {
        return propertyComponentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Componente não encontrado no sistema: " + id));
    }

    @Transactional
    public PropertyComponent update(String id, UpdateComponentDTO data) {
        PropertyComponent component = propertyComponentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Componente não encontrado no sistema: " + id));

        component.updateValues(data);

        return propertyComponentRepository.save(component);
    }

    @Transactional
    public void deleteById(String id) {
        PropertyComponent component = propertyComponentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Componente não encontrado no sistema: " + id));

        propertyComponentRepository.delete(component);
    }
}
