package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class EnrollRequest {

    @NotBlank(message = "memberId is required")
    private String memberId;
}