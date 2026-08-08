package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.CreateEquipmentRequest;
import org.example.gymbackend.dto.request.UpdateEquipmentStatusRequest;
import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.entity.Equipment;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public EquipmentResponse createEquipment(CreateEquipmentRequest request) {
        Equipment equipment = Equipment.builder()
                .name(request.getName())
                .status(Status.EquipmentStatus.AVAILABLE)
                .purchaseDate(request.getPurchaseDate() != null ? request.getPurchaseDate() : LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment);
        return EquipmentResponse.fromEntity(equipment);
    }

    public List<EquipmentResponse> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(EquipmentResponse::fromEntity)
                .toList();
    }

    public EquipmentResponse getEquipmentById(String id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> GymApiException.equipmentNotFound(id));
        return EquipmentResponse.fromEntity(equipment);
    }

    @Transactional
    public EquipmentResponse updateStatus(String id, UpdateEquipmentStatusRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> GymApiException.equipmentNotFound(id));

        equipment.setStatus(request.getStatus());
        equipmentRepository.save(equipment);
        return EquipmentResponse.fromEntity(equipment);
    }
}
