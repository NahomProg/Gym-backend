package org.example.gymbackend.controller;

import jakarta.validation.Valid;
import org.example.gymbackend.dto.request.CreateEquipmentRequest;
import org.example.gymbackend.dto.request.ResolveMaintenanceRequest;
import org.example.gymbackend.dto.request.StartMaintenanceRequest;
import org.example.gymbackend.dto.request.UpdateEquipmentStatusRequest;
import org.example.gymbackend.dto.response.EquipmentMaintenanceResponse;
import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.createEquipment(request));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable String id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<EquipmentResponse> updateStatus(@PathVariable String id,
                                                            @Valid @RequestBody UpdateEquipmentStatusRequest request) {
        return ResponseEntity.ok(equipmentService.updateStatus(id, request));
    }

    @PostMapping("/{id}/maintenance")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<EquipmentMaintenanceResponse> startMaintenance(@PathVariable String id,
                                                                           @Valid @RequestBody StartMaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.startMaintenance(id, request));
    }

    @PatchMapping("/{id}/maintenance/{recordId}/resolve")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<EquipmentMaintenanceResponse> resolveMaintenance(@PathVariable String id,
                                                                             @PathVariable String recordId,
                                                                             @Valid @RequestBody ResolveMaintenanceRequest request) {
        return ResponseEntity.ok(equipmentService.resolveMaintenance(id, recordId, request));
    }

    @GetMapping("/{id}/maintenance")
    public ResponseEntity<List<EquipmentMaintenanceResponse>> getMaintenanceHistory(@PathVariable String id) {
        return ResponseEntity.ok(equipmentService.getMaintenanceHistory(id));
    }
}
