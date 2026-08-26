package org.example.gymbackend.controller;

import jakarta.validation.*;
import org.example.gymbackend.dto.request.AssignMembershipRequest;
import org.example.gymbackend.dto.response.MembershipResponse;
import org.example.gymbackend.service.MembershipService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.*;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<MembershipResponse> assignMembership(@Valid @RequestBody AssignMembershipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipService.assignMembership(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getMembershipById(@PathVariable String id) {
        return ResponseEntity.ok(membershipService.getMembershipById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MembershipResponse>> getMembershipsByMember(@PathVariable String memberId) {
        return ResponseEntity.ok(membershipService.getMembershipsByMember(memberId));
    }
}
