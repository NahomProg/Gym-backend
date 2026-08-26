package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.EquipmentResponse;
import org.example.gymbackend.entity.Equipment;
import org.example.gymbackend.mapper.EquipmentMapper;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapperImpl implements EquipmentMapper {

    @Override
    public EquipmentResponse toResponse(Equipment entity) {
        return new EquipmentResponse(
                entity.getId(),
                entity.getName(),
                entity.getStatus(),
                entity.getPurchaseDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
