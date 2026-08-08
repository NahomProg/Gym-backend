package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;

@Data
public class CreateClassSessionRequest {

    @NotBlank(message = "className is required")
    private String className;

    private String classDescription;

    @NotNull(message = "startTime is required")
    private LocalDateTime startTime;

    @NotNull(message = "endTime is required")
    private LocalDateTime endTime;

    @NotNull(message = "maxCapacity is required")
    @Min(value = 1, message = "maxCapacity must be at least 1")
    private Integer maxCapacity;
}