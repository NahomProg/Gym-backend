package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
public class CreateMembershipPlanRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @NotNull(message = "durationDays is required")
    @Positive(message = "durationDays must be positive")
    private Integer durationDays;

    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private BigDecimal price;

    private String perks;
}
