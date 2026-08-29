package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;

@Data
public class CreateClassSessionRequest {

    @NotBlank(message = "className is required")
    @JsonProperty("class_name")
    private String className;

    @JsonProperty("class_description")
    private String classDescription;

    @NotNull(message = "startTime is required")
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @NotNull(message = "endTime is required")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @NotNull(message = "maxCapacity is required")
    @Min(value = 1, message = "maxCapacity must be at least 1")
    @JsonProperty("max_capacity")
    private Integer maxCapacity;
}

//json proporty