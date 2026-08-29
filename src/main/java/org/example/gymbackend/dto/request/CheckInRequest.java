package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckInRequest {

    @NotBlank(message = "memberId is required")
    @JsonProperty("member_id")
    private String memberId;

    @JsonProperty("class_session_id")
    private String classSessionId;
}