package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class CreateEquipmentRequest {

    @NotBlank(message = "name is required")
    private String name;

    /** Optional - defaults to now if not provided. */
    @JsonProperty("purchase_date")
    private LocalDateTime purchaseDate;
}
