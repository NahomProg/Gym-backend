package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassSessionResponse {
    private String id;
    private String className;
    private String classDescription;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private Status.ClassStatus status;
}