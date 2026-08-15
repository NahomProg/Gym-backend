package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.ClassEnrollment;
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

    public static EnrollmentResponse fromEntity(ClassEnrollment enrollment) {
        return fromEntity(enrollment, null);
    }

    public static EnrollmentResponse fromEntity(ClassEnrollment enrollment, Integer waitlistPosition) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getMember().getId(),
                enrollment.getMember().getFirstName() + " " + enrollment.getMember().getLastName(),
                enrollment.getClassSession().getId(),
                enrollment.getClassSession().getClassName(),
                enrollment.isWaitlisted(),
                waitlistPosition,
                enrollment.getStatus(),
                enrollment.getCreatedAt()
        );
    }
}