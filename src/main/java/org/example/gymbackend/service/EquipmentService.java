package org.example.gymbackend.service;

import java.util.List;
import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.dto.response.EquipmentMaintenanceResponse;
import org.example.gymbackend.dto.request.CreateEquipmentRequest;
import org.example.gymbackend.dto.request.UpdateEquipmentStatusRequest;
import org.example.gymbackend.dto.request.StartMaintenanceRequest;
import org.example.gymbackend.dto.request.ResolveMaintenanceRequest;

public interface EquipmentService {

    EquipmentResponse createEquipment(CreateEquipmentRequest request);
    List<EquipmentResponse> getAllEquipment();
    EquipmentResponse getEquipmentById(String id);
    EquipmentResponse updateStatus(String id, UpdateEquipmentStatusRequest request);
    EquipmentMaintenanceResponse startMaintenance(String equipmentId, StartMaintenanceRequest request);
    EquipmentMaintenanceResponse resolveMaintenance(String equipmentId, String recordId, ResolveMaintenanceRequest request);
    List<EquipmentMaintenanceResponse> getMaintenanceHistory(String equipmentId);
}
