package org.example.gymbackend.dto.response;

import lombok.*;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String role;
}