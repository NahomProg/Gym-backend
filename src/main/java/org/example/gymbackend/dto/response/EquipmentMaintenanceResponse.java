package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.EquipmentMaintenanceRecord;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentMaintenanceResponse {
    private String id;
    private String equipmentId;
    private String equipmentName;
    private String reason;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
    private String notes;
    private boolean open;

    public static EquipmentMaintenanceResponse fromEntity(EquipmentMaintenanceRecord record) {
        return new EquipmentMaintenanceResponse(
                record.getId(),
                record.getEquipment().getId(),
                record.getEquipment().getName(),
                record.getReason(),
                record.getReportedAt(),
                record.getResolvedAt(),
                record.getNotes(),
                record.getResolvedAt() == null
        );
    }
}
