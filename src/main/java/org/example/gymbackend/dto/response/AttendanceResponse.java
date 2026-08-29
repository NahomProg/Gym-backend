package org.example.gymbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private String id;
    @JsonProperty("member_id")
    private String memberId;
    @JsonProperty("member_name")
    private String memberName;
    @JsonProperty("class_session_id")
    private String classSessionId;
    @JsonProperty("class_name")
    private String className;
    @JsonProperty("check_in_time")
    private LocalDateTime checkInTime;
    @JsonProperty("check_out_time")
    private LocalDateTime checkOutTime;
    @JsonProperty("duration_minutes")
    private Integer durationMinutes;
}