package org.example.gymbackend.service.impl;

import org.example.gymbackend.service.MembershipPlanService;
import org.example.gymbackend.mapper.MembershipPlanMapper;

import org.example.gymbackend.dto.request.CreateMembershipPlanRequest;
import org.example.gymbackend.dto.response.MembershipPlanResponse;
import org.example.gymbackend.entity.MembershipPlan;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.MembershipPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MembershipPlanServiceImpl implements MembershipPlanService {
    public MembershipPlanServiceImpl(
            MembershipPlanMapper membershipPlanMapper,
                             MembershipPlanRepository membershipPlanRepository) {
        this.membershipPlanMapper = membershipPlanMapper;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    private final MembershipPlanMapper membershipPlanMapper;

    private final MembershipPlanRepository membershipPlanRepository;


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
        return membershipPlanMapper.toResponse(plan);
    }

    public List<MembershipPlanResponse> getAllPlans() {
        return membershipPlanRepository.findAll().stream()
                .map(membershipPlanMapper::toResponse)
                .toList();
    }
}
