package org.example.gymbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassSessionResponse {
    private String id;
    @JsonProperty("class_name")
    private String className;
    @JsonProperty("class_description")
    private String classDescription;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;
    @JsonProperty("max_capacity")
    private Integer maxCapacity;
    @JsonProperty("current_enrollment")
    private Integer currentEnrollment;
    private Status.ClassStatus status;
}