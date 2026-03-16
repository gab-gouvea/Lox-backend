package br.com.lox.domain.scheduledmaintenance.repository;

import br.com.lox.domain.scheduledmaintenance.entity.ScheduledMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduledMaintenanceRepository extends JpaRepository<ScheduledMaintenance, String> {

    List<ScheduledMaintenance> findByPropriedadeId(String propriedadeId);

    List<ScheduledMaintenance> findByPropriedadeIdAndConfirmada(String propriedadeId, Boolean confirmada);

    List<ScheduledMaintenance> findByConfirmadaTrueAndDataConclusaoBefore(LocalDate date);
}
