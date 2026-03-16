package br.com.lox.domain.maintenancerecord.repository;

import br.com.lox.domain.maintenancerecord.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, String> {

    List<MaintenanceRecord> findByDataBetween(LocalDate startDate, LocalDate endDate);

    List<MaintenanceRecord> findByPropriedadeIdAndDataBetween(String propriedadeId, LocalDate startDate, LocalDate endDate);

    List<MaintenanceRecord> findByPropriedadeId(String propriedadeId);
}
