package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class EnrollRequest {

    @NotBlank(message = "memberId is required")
    @JsonProperty("member_id")
    private String memberId;
}