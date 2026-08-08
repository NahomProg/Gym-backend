package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckInRequest {

    @NotBlank(message = "memberId is required")
    private String memberId;

    private String classSessionId;
}