package br.com.lox.domain.maintenancerecord.controller;

import br.com.lox.domain.maintenancerecord.dto.CreateMaintenanceRecordDTO;
import br.com.lox.domain.maintenancerecord.dto.UpdateMaintenanceRecordDTO;
import br.com.lox.domain.maintenancerecord.entity.MaintenanceRecord;
import br.com.lox.domain.maintenancerecord.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecord> create(@RequestBody @Valid CreateMaintenanceRecordDTO data) {
        MaintenanceRecord record = maintenanceRecordService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceRecord>> findAll(
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<MaintenanceRecord> records = maintenanceRecordService.findAll(propertyId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> update(@PathVariable String id,
                                                     @RequestBody @Valid UpdateMaintenanceRecordDTO data) {
        MaintenanceRecord record = maintenanceRecordService.update(id, data);
        return ResponseEntity.ok(record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        maintenanceRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
