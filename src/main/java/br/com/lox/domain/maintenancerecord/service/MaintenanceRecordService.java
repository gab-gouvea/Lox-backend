package br.com.lox.domain.maintenancerecord.service;

import br.com.lox.domain.maintenancerecord.dto.CreateMaintenanceRecordDTO;
import br.com.lox.domain.maintenancerecord.dto.UpdateMaintenanceRecordDTO;
import br.com.lox.domain.maintenancerecord.entity.MaintenanceRecord;
import br.com.lox.domain.maintenancerecord.repository.MaintenanceRecordRepository;
import br.com.lox.exceptions.MaintenanceRecordNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceRecordService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;

    public MaintenanceRecordService(MaintenanceRecordRepository maintenanceRecordRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
    }

    @Transactional
    public MaintenanceRecord create(CreateMaintenanceRecordDTO data) {
        var entity = new MaintenanceRecord(
                data.propriedadeId(),
                data.componenteId(),
                data.nomeServico(),
                data.prestador(),
                data.data(),
                data.valor(),
                data.pago()
        );

        return maintenanceRecordRepository.save(entity);
    }

    public List<MaintenanceRecord> findAll(String propertyId, LocalDate startDate, LocalDate endDate) {
        boolean hasProperty = propertyId != null;
        boolean hasDates = startDate != null && endDate != null;

        if (hasProperty && hasDates) {
            return maintenanceRecordRepository.findByPropriedadeIdAndDataBetween(propertyId, startDate, endDate);
        }
        if (hasDates) {
            return maintenanceRecordRepository.findByDataBetween(startDate, endDate);
        }
        if (hasProperty) {
            return maintenanceRecordRepository.findByPropriedadeId(propertyId);
        }
        return maintenanceRecordRepository.findAll();
    }

    public MaintenanceRecord findById(String id) {
        return maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException("Registro de manutenção não encontrado no sistema: " + id));
    }

    @Transactional
    public MaintenanceRecord update(String id, UpdateMaintenanceRecordDTO data) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException("Registro de manutenção não encontrado no sistema: " + id));

        record.updateValues(data);

        return maintenanceRecordRepository.save(record);
    }

    @Transactional
    public void deleteById(String id) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException("Registro de manutenção não encontrado no sistema: " + id));

        maintenanceRecordRepository.delete(record);
    }
}
