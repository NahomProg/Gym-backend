package org.example.gymbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private String id;
    @JsonProperty("member_id")
    private String memberId;
    @JsonProperty("member_name")
    private String memberName;
    @JsonProperty("class_session_id")
    private String classSessionId;
    @JsonProperty("class_name")
    private String className;
    private boolean waitlisted;
    @JsonProperty("waitlist_position")
    private Integer waitlistPosition;
    private Status.EnrollmentStatus status;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
