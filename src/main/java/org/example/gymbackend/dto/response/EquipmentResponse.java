package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

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
}
