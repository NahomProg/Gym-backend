package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private String id;
    private String memberId;
    private String memberName;
    private String classSessionId;
    private String className;
    private boolean waitlisted;
    private Integer waitlistPosition;
    private Status.EnrollmentStatus status;
    private LocalDateTime createdAt;
}
