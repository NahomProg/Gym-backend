package org.example.gymbackend.config;

import lombok.RequiredArgsConstructor;
import org.example.gymbackend.entity.Role;
import org.example.gymbackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runs once, every time the app starts.
 * Makes sure the 3 roles this app depends on (ADMIN, TRAINER, MEMBER) always
 * exist in the database, so features like registration (which assigns the
 * MEMBER role by name) never fail just because no one seeded the table yet.
 */
@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> requiredRoles = List.of("ADMIN", "TRAINER", "MEMBER");

        for (String roleName : requiredRoles) {
            boolean exists = roleRepository.findByName(roleName).isPresent();
            if (!exists) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();
                roleRepository.save(role);
            }
        }
    }
}
