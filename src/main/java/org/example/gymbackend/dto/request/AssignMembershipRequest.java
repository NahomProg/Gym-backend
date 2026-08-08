package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
public class AssignMembershipRequest {

    @NotBlank(message = "memberId is required")
    private String memberId;

    @NotBlank(message = "planId is required")
    private String planId;

    /** Optional - defaults to today if not provided. */
    private LocalDate startDate;
}
