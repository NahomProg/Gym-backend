package org.example.gymbackend.controller;

import jakarta.validation.*;
import org.example.gymbackend.dto.request.CheckInRequest;
import org.example.gymbackend.dto.response.AttendanceResponse;
import org.example.gymbackend.service.AttendanceService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.checkIn(request));
    }

    @PostMapping("/check-out/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable String memberId) {
        return ResponseEntity.ok(attendanceService.checkOut(memberId));
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<AttendanceResponse>> getHistory(@PathVariable String memberId) {
        return ResponseEntity.ok(attendanceService.getHistoryForMember(memberId));
    }
}