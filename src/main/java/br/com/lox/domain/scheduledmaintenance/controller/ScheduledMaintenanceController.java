package br.com.lox.domain.scheduledmaintenance.controller;

import br.com.lox.domain.scheduledmaintenance.dto.ConfirmScheduledMaintenanceDTO;
import br.com.lox.domain.scheduledmaintenance.dto.CreateScheduledMaintenanceDTO;
import br.com.lox.domain.scheduledmaintenance.dto.UpdateScheduledMaintenanceDTO;
import br.com.lox.domain.scheduledmaintenance.entity.ScheduledMaintenance;
import br.com.lox.domain.scheduledmaintenance.service.ScheduledMaintenanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScheduledMaintenanceController {

    private final ScheduledMaintenanceService scheduledMaintenanceService;

    public ScheduledMaintenanceController(ScheduledMaintenanceService scheduledMaintenanceService) {
        this.scheduledMaintenanceService = scheduledMaintenanceService;
    }

    @GetMapping("/api/properties/{propertyId}/scheduled-maintenances")
    public ResponseEntity<List<ScheduledMaintenance>> findByPropertyId(@PathVariable String propertyId) {
        List<ScheduledMaintenance> list = scheduledMaintenanceService.findByPropertyId(propertyId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/api/properties/{propertyId}/scheduled-maintenances")
    public ResponseEntity<ScheduledMaintenance> create(@PathVariable String propertyId,
                                                        @RequestBody @Valid CreateScheduledMaintenanceDTO data) {
        ScheduledMaintenance entity = scheduledMaintenanceService.create(propertyId, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @PutMapping("/api/scheduled-maintenances/{id}")
    public ResponseEntity<ScheduledMaintenance> update(@PathVariable String id,
                                                        @RequestBody @Valid UpdateScheduledMaintenanceDTO data) {
        ScheduledMaintenance entity = scheduledMaintenanceService.update(id, data);
        return ResponseEntity.ok(entity);
    }

    @PatchMapping("/api/scheduled-maintenances/{id}/confirm")
    public ResponseEntity<ScheduledMaintenance> confirm(@PathVariable String id,
                                                         @RequestBody @Valid ConfirmScheduledMaintenanceDTO data) {
        ScheduledMaintenance entity = scheduledMaintenanceService.confirm(id, data);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/api/scheduled-maintenances/pending")
    public ResponseEntity<List<ScheduledMaintenance>> findAllPending() {
        List<ScheduledMaintenance> list = scheduledMaintenanceService.findAllPending();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/api/scheduled-maintenances/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        scheduledMaintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
