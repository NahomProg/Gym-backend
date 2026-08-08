package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.gymbackend.entity.Status;

@Data
public class UpdateEquipmentStatusRequest {

    @NotNull(message = "status is required")
    private Status.EquipmentStatus status;
}
