package org.example.gymbackend.service;

import java.util.*;
import org.example.gymbackend.dto.response.AuthResponse;
import org.example.gymbackend.dto.request.LoginRequest;
import org.example.gymbackend.dto.request.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
