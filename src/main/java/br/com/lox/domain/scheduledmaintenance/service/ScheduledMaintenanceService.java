package br.com.lox.domain.scheduledmaintenance.service;

import br.com.lox.domain.maintenancerecord.dto.CreateMaintenanceRecordDTO;
import br.com.lox.domain.maintenancerecord.service.MaintenanceRecordService;
import br.com.lox.domain.scheduledmaintenance.dto.ConfirmScheduledMaintenanceDTO;
import br.com.lox.domain.scheduledmaintenance.dto.CreateScheduledMaintenanceDTO;
import br.com.lox.domain.scheduledmaintenance.dto.UpdateScheduledMaintenanceDTO;
import br.com.lox.domain.scheduledmaintenance.entity.ScheduledMaintenance;
import br.com.lox.domain.scheduledmaintenance.repository.ScheduledMaintenanceRepository;
import br.com.lox.exceptions.BusinessRuleException;
import br.com.lox.exceptions.ScheduledMaintenanceNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledMaintenanceService {

    private final ScheduledMaintenanceRepository scheduledMaintenanceRepository;
    private final MaintenanceRecordService maintenanceRecordService;

    public ScheduledMaintenanceService(ScheduledMaintenanceRepository scheduledMaintenanceRepository,
                                        MaintenanceRecordService maintenanceRecordService) {
        this.scheduledMaintenanceRepository = scheduledMaintenanceRepository;
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @Transactional
    public ScheduledMaintenance create(String propriedadeId, CreateScheduledMaintenanceDTO data) {
        var entity = new ScheduledMaintenance(
                propriedadeId,
                data.nome(),
                data.dataPrevista(),
                data.prestador()
        );
        return scheduledMaintenanceRepository.save(entity);
    }

    public List<ScheduledMaintenance> findByPropertyId(String propriedadeId) {
        return scheduledMaintenanceRepository.findByPropriedadeId(propriedadeId);
    }

    public List<ScheduledMaintenance> findPendingByPropertyId(String propriedadeId) {
        return scheduledMaintenanceRepository.findByPropriedadeIdAndConfirmada(propriedadeId, false);
    }

    public List<ScheduledMaintenance> findAllPending() {
        return scheduledMaintenanceRepository.findByConfirmadaFalse();
    }

    public ScheduledMaintenance findById(String id) {
        return scheduledMaintenanceRepository.findById(id)
                .orElseThrow(() -> new ScheduledMaintenanceNotFoundException("Manutenção agendada não encontrada: " + id));
    }

    @Transactional
    public ScheduledMaintenance update(String id, UpdateScheduledMaintenanceDTO data) {
        ScheduledMaintenance entity = scheduledMaintenanceRepository.findById(id)
                .orElseThrow(() -> new ScheduledMaintenanceNotFoundException("Manutenção agendada não encontrada: " + id));

        if (entity.getConfirmada()) {
            throw new BusinessRuleException("Não é possível editar uma manutenção já confirmada");
        }

        entity.updateValues(data);
        return scheduledMaintenanceRepository.save(entity);
    }

    @Transactional
    public ScheduledMaintenance confirm(String id, ConfirmScheduledMaintenanceDTO data) {
        ScheduledMaintenance entity = scheduledMaintenanceRepository.findById(id)
                .orElseThrow(() -> new ScheduledMaintenanceNotFoundException("Manutenção agendada não encontrada: " + id));

        if (entity.getConfirmada()) {
            throw new BusinessRuleException("Manutenção já foi confirmada");
        }

        entity.confirmar(data.valor(), data.prestador());
        scheduledMaintenanceRepository.save(entity);

        // Lanca no relatorio de manutencoes
        var recordDTO = new CreateMaintenanceRecordDTO(
                entity.getPropriedadeId(),
                "agendada", // sem componente vinculado
                entity.getNome(),
                entity.getPrestador(),
                LocalDate.now(),
                data.valor() != null ? data.valor() : java.math.BigDecimal.ZERO,
                false
        );
        maintenanceRecordService.create(recordDTO);

        return entity;
    }

    @Scheduled(cron = "0 0 3 * * *") // Roda todo dia às 3h da manhã
    @Transactional
    public void deleteOldConfirmed() {
        var cutoff = LocalDate.now().minusDays(3);
        var old = scheduledMaintenanceRepository.findByConfirmadaTrueAndDataConclusaoBefore(cutoff);
        if (!old.isEmpty()) {
            scheduledMaintenanceRepository.deleteAll(old);
        }
    }

    @Transactional
    public void deleteById(String id) {
        ScheduledMaintenance entity = scheduledMaintenanceRepository.findById(id)
                .orElseThrow(() -> new ScheduledMaintenanceNotFoundException("Manutenção agendada não encontrada: " + id));

        scheduledMaintenanceRepository.delete(entity);
    }
}
