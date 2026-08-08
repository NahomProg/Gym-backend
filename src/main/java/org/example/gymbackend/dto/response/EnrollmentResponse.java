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
    private Status.EnrollmentStatus status;
    private LocalDateTime createdAt;

    public static EnrollmentResponse fromEntity(ClassEnrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getMember().getId(),
                enrollment.getMember().getFirstName() + " " + enrollment.getMember().getLastName(),
                enrollment.getClassSession().getId(),
                enrollment.getClassSession().getClassName(),
                enrollment.isWaitlisted(),
                enrollment.getStatus(),
                enrollment.getCreatedAt()
        );
    }
}