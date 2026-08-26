package org.example.gymbackend.dto.response;

import lombok.*;
import java.time.*;

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
}
