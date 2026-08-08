package org.example.gymbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gymbackend.entity.Equipment;
import org.example.gymbackend.entity.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponse {
    private String id;
    private String name;
    private Status.EquipmentStatus status;
    private LocalDateTime purchaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EquipmentResponse fromEntity(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getStatus(),
                equipment.getPurchaseDate(),
                equipment.getCreatedAt(),
                equipment.getUpdatedAt()
        );
    }
}
