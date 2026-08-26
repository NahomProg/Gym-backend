package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.EquipmentMaintenanceResponse;
import org.example.gymbackend.entity.EquipmentMaintenanceRecord;
import org.example.gymbackend.mapper.EquipmentMaintenanceMapper;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMaintenanceMapperImpl implements EquipmentMaintenanceMapper {

    @Override
    public EquipmentMaintenanceResponse toResponse(EquipmentMaintenanceRecord entity) {
        return new EquipmentMaintenanceResponse(
                entity.getId(),
                entity.getEquipment().getId(),
                entity.getEquipment().getName(),
                entity.getReason(),
                entity.getReportedAt(),
                entity.getResolvedAt(),
                entity.getNotes(),
                entity.getResolvedAt() == null
        );
    }
}
