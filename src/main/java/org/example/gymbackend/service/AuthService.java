package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.LoginRequest;
import org.example.gymbackend.dto.request.RegisterRequest;
import org.example.gymbackend.dto.response.AuthResponse;
import org.example.gymbackend.entity.Role;
import org.example.gymbackend.entity.User;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.RoleRepository;
import org.example.gymbackend.repository.UserRepository;
import org.example.gymbackend.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw GymApiException.emailAlreadyExists(request.getEmail());
        }

        Role memberRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> GymApiException.roleNotFound("MEMBER"));

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(memberRole)
                .enabled(true)
                .locked(false)
                .build();

        userRepository.save(user);

        String token = tokenProvider.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> GymApiException.userNotFound(request.getEmail()));

        String token = tokenProvider.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
}