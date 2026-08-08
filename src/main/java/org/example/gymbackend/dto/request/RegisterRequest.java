package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class RegisterRequest {

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;
}
