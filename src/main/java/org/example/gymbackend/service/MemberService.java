package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.CreateMemberRequest;
import org.example.gymbackend.dto.request.UpdateMemberRequest;
import org.example.gymbackend.dto.response.MemberResponse;
import org.example.gymbackend.entity.Member;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw GymApiException.memberEmailAlreadyExists(request.getEmail());
        }
        if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw GymApiException.memberPhoneAlreadyExists(request.getPhoneNumber());
        }

        Member member = Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .status(Status.MemberStatus.ACTIVE)
                .joinDate(LocalDate.now())
                .build();

        memberRepository.save(member);
        return MemberResponse.fromEntity(member);
    }

    public MemberResponse getMemberById(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> GymApiException.memberNotFound(id));
        return MemberResponse.fromEntity(member);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::fromEntity)
                .toList();
    }

    @Transactional
    public MemberResponse updateMember(String id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> GymApiException.memberNotFound(id));

        if (request.getFirstName() != null) {
            member.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            member.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(member.getPhoneNumber())) {
            if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw GymApiException.memberPhoneAlreadyExists(request.getPhoneNumber());
            }
            member.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDateOfBirth() != null) {
            member.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }

        memberRepository.save(member);
        return MemberResponse.fromEntity(member);
    }
}
