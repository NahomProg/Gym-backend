package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.CreateMembershipPlanRequest;
import org.example.gymbackend.dto.response.MembershipPlanResponse;
import org.example.gymbackend.entity.MembershipPlan;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.MembershipPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;

    public MembershipPlanService(MembershipPlanRepository membershipPlanRepository) {
        this.membershipPlanRepository = membershipPlanRepository;
    }

    @Transactional
    public MembershipPlanResponse createPlan(CreateMembershipPlanRequest request) {
        if (membershipPlanRepository.findByName(request.getName()).isPresent()) {
            throw GymApiException.membershipPlanAlreadyExists(request.getName());
        }

        MembershipPlan plan = MembershipPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .durationDays(request.getDurationDays())
                .price(request.getPrice())
                .perks(request.getPerks())
                .build();

        membershipPlanRepository.save(plan);
        return MembershipPlanResponse.fromEntity(plan);
    }

    public List<MembershipPlanResponse> getAllPlans() {
        return membershipPlanRepository.findAll().stream()
                .map(MembershipPlanResponse::fromEntity)
                .toList();
    }
}
