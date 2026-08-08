package org.example.gymbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class LoginRequest {

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
