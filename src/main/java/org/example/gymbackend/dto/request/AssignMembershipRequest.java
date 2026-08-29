package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
public class AssignMembershipRequest {

    @NotBlank(message = "memberId is required")
    @JsonProperty("member_id")
    private String memberId;

    @NotBlank(message = "planId is required")
    @JsonProperty("plan_id")
    private String planId;

    /** Optional - defaults to today if not provided. */
    @JsonProperty("start_date")
    private LocalDate startDate;
}
