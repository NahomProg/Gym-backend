package org.example.gymbackend.controller;

import jakarta.validation.*;
import org.example.gymbackend.dto.request.CreateClassSessionRequest;
import org.example.gymbackend.dto.request.EnrollRequest;
import org.example.gymbackend.dto.response.ClassSessionResponse;
import org.example.gymbackend.dto.response.EnrollmentResponse;
import org.example.gymbackend.service.ClassSessionService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/classes")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    public ClassSessionController(ClassSessionService classSessionService) {
        this.classSessionService = classSessionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<ClassSessionResponse> createClass(@Valid @RequestBody CreateClassSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classSessionService.createClass(request));
    }

    @GetMapping
    public ResponseEntity<List<ClassSessionResponse>> getAllClasses() {
        return ResponseEntity.ok(classSessionService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassSessionResponse> getClassById(@PathVariable String id) {
        return ResponseEntity.ok(classSessionService.getClassById(id));
    }

    @PostMapping("/{id}/enroll")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable String id, @Valid @RequestBody EnrollRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classSessionService.enroll(id, request));
    }

    @DeleteMapping("/{id}/enroll/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<EnrollmentResponse> cancelEnrollment(@PathVariable String id, @PathVariable String memberId) {
        return ResponseEntity.ok(classSessionService.cancelEnrollment(id, memberId));
    }

    @GetMapping("/{id}/waitlist")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<EnrollmentResponse>> getWaitlist(@PathVariable String id) {
        return ResponseEntity.ok(classSessionService.getWaitlist(id));
    }

    @GetMapping("/{id}/enrollments")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollments(@PathVariable String id) {
        return ResponseEntity.ok(classSessionService.getEnrollmentsForClass(id));
    }
}