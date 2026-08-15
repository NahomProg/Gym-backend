package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.CreateEquipmentRequest;
import org.example.gymbackend.dto.request.ResolveMaintenanceRequest;
import org.example.gymbackend.dto.request.StartMaintenanceRequest;
import org.example.gymbackend.dto.request.UpdateEquipmentStatusRequest;
import org.example.gymbackend.dto.response.EquipmentMaintenanceResponse;
import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.entity.Equipment;
import org.example.gymbackend.entity.EquipmentMaintenanceRecord;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.EquipmentMaintenanceRecordRepository;
import org.example.gymbackend.repository.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMaintenanceRecordRepository maintenanceRecordRepository;

    public EquipmentService(EquipmentRepository equipmentRepository,
                             EquipmentMaintenanceRecordRepository maintenanceRecordRepository) {
        this.equipmentRepository = equipmentRepository;
        this.maintenanceRecordRepository = maintenanceRecordRepository;
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

        boolean movingIntoMaintenance = request.getStatus() == Status.EquipmentStatus.UNDER_MAINTENANCE;
        boolean movingOutOfMaintenance = equipment.getStatus() == Status.EquipmentStatus.UNDER_MAINTENANCE
                && request.getStatus() != Status.EquipmentStatus.UNDER_MAINTENANCE;

        if (movingIntoMaintenance || movingOutOfMaintenance) {
            throw GymApiException.statusChangeRequiresMaintenanceWorkflow(id);
        }

        equipment.setStatus(request.getStatus());
        equipmentRepository.save(equipment);
        return EquipmentResponse.fromEntity(equipment);
    }

    @Transactional
    public EquipmentMaintenanceResponse startMaintenance(String equipmentId, StartMaintenanceRequest request) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> GymApiException.equipmentNotFound(equipmentId));

        if (maintenanceRecordRepository.existsByEquipmentIdAndResolvedAtIsNull(equipmentId)) {
            throw GymApiException.equipmentAlreadyUnderMaintenance(equipmentId);
        }

        EquipmentMaintenanceRecord record = EquipmentMaintenanceRecord.builder()
                .equipment(equipment)
                .reason(request.getReason())
                .reportedAt(LocalDateTime.now())
                .build();

        maintenanceRecordRepository.save(record);

        equipment.setStatus(Status.EquipmentStatus.UNDER_MAINTENANCE);
        equipmentRepository.save(equipment);

        return EquipmentMaintenanceResponse.fromEntity(record);
    }

    @Transactional
    public EquipmentMaintenanceResponse resolveMaintenance(String equipmentId, String recordId, ResolveMaintenanceRequest request) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> GymApiException.equipmentNotFound(equipmentId));

        EquipmentMaintenanceRecord record = maintenanceRecordRepository.findByIdAndEquipmentId(recordId, equipmentId)
                .orElseThrow(() -> GymApiException.maintenanceRecordNotFound(recordId));

        if (record.getResolvedAt() != null) {
            throw GymApiException.maintenanceAlreadyResolved(recordId);
        }

        record.setResolvedAt(LocalDateTime.now());
        record.setNotes(request.getNotes());
        maintenanceRecordRepository.save(record);

        equipment.setStatus(Status.EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        return EquipmentMaintenanceResponse.fromEntity(record);
    }

    public List<EquipmentMaintenanceResponse> getMaintenanceHistory(String equipmentId) {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw GymApiException.equipmentNotFound(equipmentId);
        }

        return maintenanceRecordRepository.findByEquipmentIdOrderByReportedAtDesc(equipmentId).stream()
                .map(EquipmentMaintenanceResponse::fromEntity)
                .toList();
    }
}
