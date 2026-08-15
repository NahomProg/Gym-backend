package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class StartMaintenanceRequest {

    @NotBlank(message = "reason is required")
    private String reason;
}
