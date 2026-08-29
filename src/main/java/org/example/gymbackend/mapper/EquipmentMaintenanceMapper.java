package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.EquipmentMaintenanceResponse;
import org.example.gymbackend.entity.EquipmentMaintenanceRecord;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EquipmentMaintenanceMapper {

    @Mapping(target = "equipmentId", source = "equipment.id")
    @Mapping(target = "equipmentName", source = "equipment.name")
    @Mapping(target = "open", expression = "java(entity.getResolvedAt() == null)")
    EquipmentMaintenanceResponse toResponse(EquipmentMaintenanceRecord entity);
}
