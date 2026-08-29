package org.example.gymbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentMaintenanceResponse {
    private String id;
    @JsonProperty("equipment_id")
    private String equipmentId;
    @JsonProperty("equipment_name")
    private String equipmentName;
    private String reason;
    @JsonProperty("reported_at")
    private LocalDateTime reportedAt;
    @JsonProperty("resolved_at")
    private LocalDateTime resolvedAt;
    private String notes;
    private boolean open;
}
