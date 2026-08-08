package org.example.gymbackend.controller;

import jakarta.validation.Valid;
import org.example.gymbackend.dto.request.CreateEquipmentRequest;
import org.example.gymbackend.dto.request.UpdateEquipmentStatusRequest;
import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EquipmentResponse> updateStatus(@PathVariable String id,
                                                            @Valid @RequestBody UpdateEquipmentStatusRequest request) {
        return ResponseEntity.ok(equipmentService.updateStatus(id, request));
    }
}
