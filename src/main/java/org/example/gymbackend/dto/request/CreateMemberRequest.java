package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
public class CreateMemberRequest {

    @NotBlank(message = "firstName is required")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotBlank(message = "phoneNumber is required")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
}
