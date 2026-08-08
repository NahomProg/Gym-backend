package org.example.gymbackend.controller;

import jakarta.validation.Valid;
import org.example.gymbackend.dto.request.CreateMembershipPlanRequest;
import org.example.gymbackend.dto.response.MembershipPlanResponse;
import org.example.gymbackend.service.MembershipPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membership-plans")
public class MembershipPlanController {

    private final MembershipPlanService membershipPlanService;

    public MembershipPlanController(MembershipPlanService membershipPlanService) {
        this.membershipPlanService = membershipPlanService;
    }

    @PostMapping
    public ResponseEntity<MembershipPlanResponse> createPlan(@Valid @RequestBody CreateMembershipPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipPlanService.createPlan(request));
    }

    @GetMapping
    public ResponseEntity<List<MembershipPlanResponse>> getAllPlans() {
        return ResponseEntity.ok(membershipPlanService.getAllPlans());
    }
}
