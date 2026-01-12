package br.com.lox.domain.component.service;

import br.com.lox.domain.component.dto.CreateComponentData;
import br.com.lox.domain.component.dto.UpdateComponentData;
import br.com.lox.domain.component.entity.Component;
import br.com.lox.domain.component.repository.ComponentRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.ComponentNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;

    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Transactional
    public Component create(@Valid CreateComponentData data) {
        if (componentRepository.existsByName(data.name())) {
            throw new BusinessRuleException("Componente já cadastrado com o nome " + data.name());
        }

        var entity = new Component(
                data.name(),
                data.lastMaintenance(),
                data.nextMaintenance(),
                data.maintenanceDays(),
                data.maintenanceCost(),
                data.notes()
        );

        return componentRepository.save(entity);
    }

    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    public Component findById(String id) {
        return componentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Componente não encontrado no sistema " + id));
    }

    @Transactional
    public Component update(String id, UpdateComponentData data) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Componente não foi encontrado no sistema " + id));

        component.updateValues(data);

        return componentRepository.save(component);
    }

    @Transactional
    public void deleteById(String id) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Componente não encontrado no sistema " + id));

        componentRepository.delete(component);
    }
}
