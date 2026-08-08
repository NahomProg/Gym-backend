package org.example.gymbackend.dto.response;

import lombok.*;
import org.example.gymbackend.entity.Attendance;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private String id;
    private String memberId;
    private String memberName;
    private String classSessionId;
    private String className;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer durationMinutes;

    public static AttendanceResponse fromEntity(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getMember().getId(),
                attendance.getMember().getFirstName() + " " + attendance.getMember().getLastName(),
                attendance.getClassSession() != null ? attendance.getClassSession().getId() : null,
                attendance.getClassSession() != null ? attendance.getClassSession().getClassName() : null,
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getDurationMinutes()
        );
    }
}