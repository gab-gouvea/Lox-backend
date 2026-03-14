package br.com.lox.domain.maintenancerecord.repository;

import br.com.lox.domain.maintenancerecord.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, String> {

    @Query("SELECT m FROM MaintenanceRecord m WHERE " +
            "(:propriedadeId IS NULL OR m.propriedadeId = :propriedadeId) AND " +
            "(:startDate IS NULL OR m.data >= :startDate) AND " +
            "(:endDate IS NULL OR m.data <= :endDate)")
    List<MaintenanceRecord> findWithFilters(
            @Param("propriedadeId") String propriedadeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
