package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.AssignMembershipRequest;
import org.example.gymbackend.dto.response.MembershipResponse;
import org.example.gymbackend.entity.Member;
import org.example.gymbackend.entity.Membership;
import org.example.gymbackend.entity.MembershipPlan;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.MemberRepository;
import org.example.gymbackend.repository.MembershipPlanRepository;
import org.example.gymbackend.repository.MembershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    public MembershipService(MembershipRepository membershipRepository,
                             MemberRepository memberRepository,
                             MembershipPlanRepository membershipPlanRepository) {
        this.membershipRepository = membershipRepository;
        this.memberRepository = memberRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    @Transactional
    public MembershipResponse assignMembership(AssignMembershipRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> GymApiException.memberNotFound(request.getMemberId()));

        if (member.getStatus() != Status.MemberStatus.ACTIVE) {
            throw GymApiException.memberNotActive(request.getMemberId());
        }

        MembershipPlan plan = membershipPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> GymApiException.membershipPlanNotFound(request.getPlanId()));

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = startDate.plusDays(plan.getDurationDays());

        Membership membership = Membership.builder()
                .member(member)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.MembershipStatus.ACTIVE)
                .build();

        membershipRepository.save(membership);
        return MembershipResponse.fromEntity(membership);
    }

    public MembershipResponse getMembershipById(String id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> GymApiException.membershipNotFound(id));
        return MembershipResponse.fromEntity(membership);
    }

    public List<MembershipResponse> getMembershipsByMember(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw GymApiException.memberNotFound(memberId);
        }
        return membershipRepository.findByMemberId(memberId).stream()
                .map(MembershipResponse::fromEntity)
                .toList();
    }
}