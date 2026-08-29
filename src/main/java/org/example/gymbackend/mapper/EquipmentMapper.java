package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.entity.Equipment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    EquipmentResponse toResponse(Equipment entity);
}
