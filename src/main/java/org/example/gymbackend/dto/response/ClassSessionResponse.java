package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.ClassSession;
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

    public static ClassSessionResponse fromEntity(ClassSession session) {
        return new ClassSessionResponse(
                session.getId(),
                session.getClassName(),
                session.getClassDescription(),
                session.getStartTime(),
                session.getEndTime(),
                session.getMaxCapacity(),
                session.getCurrentEnrollment(),
                session.getStatus()
        );
    }
}