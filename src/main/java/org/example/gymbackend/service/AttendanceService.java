package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.CheckInRequest;
import org.example.gymbackend.dto.response.AttendanceResponse;
import org.example.gymbackend.entity.Attendance;
import org.example.gymbackend.entity.ClassSession;
import org.example.gymbackend.entity.Member;
import org.example.gymbackend.entity.Membership;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.AttendanceRepository;
import org.example.gymbackend.repository.ClassSessionRepository;
import org.example.gymbackend.repository.MemberRepository;
import org.example.gymbackend.repository.MembershipRepository;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final ClassSessionRepository classSessionRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             MemberRepository memberRepository,
                             MembershipRepository membershipRepository,
                             ClassSessionRepository classSessionRepository) {
        this.attendanceRepository = attendanceRepository;
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
        this.classSessionRepository = classSessionRepository;
    }

    @Transactional
    public AttendanceResponse checkIn(CheckInRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> GymApiException.memberNotFound(request.getMemberId()));

        if (attendanceRepository.existsByMemberIdAndCheckOutTimeIsNull(member.getId())) {
            throw GymApiException.alreadyCheckedIn(member.getId());
        }

        if (!hasActiveMembership(member.getId())) {
            throw GymApiException.noActiveMembership(member.getId());
        }

        ClassSession classSession = null;
        if (request.getClassSessionId() != null && !request.getClassSessionId().isBlank()) {
            classSession = classSessionRepository.findById(request.getClassSessionId())
                    .orElseThrow(() -> GymApiException.classSessionNotFound(request.getClassSessionId()));
        }

        Attendance attendance = Attendance.builder()
                .member(member)
                .classSession(classSession)
                .checkInTime(LocalDateTime.now())
                .build();

        attendanceRepository.save(attendance);
        return AttendanceResponse.fromEntity(attendance);
    }

    @Transactional
    public AttendanceResponse checkOut(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw GymApiException.memberNotFound(memberId);
        }

        Attendance attendance = attendanceRepository.findByMemberIdAndCheckOutTimeIsNull(memberId)
                .orElseThrow(() -> GymApiException.noActiveCheckIn(memberId));

        LocalDateTime checkOutTime = LocalDateTime.now();
        attendance.setCheckOutTime(checkOutTime);
        attendance.setDurationMinutes((int) Duration.between(attendance.getCheckInTime(), checkOutTime).toMinutes());

        attendanceRepository.save(attendance);
        return AttendanceResponse.fromEntity(attendance);
    }

    public List<AttendanceResponse> getHistoryForMember(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw GymApiException.memberNotFound(memberId);
        }
        return attendanceRepository.findByMemberIdOrderByCheckInTimeDesc(memberId).stream()
                .map(AttendanceResponse::fromEntity)
                .toList();
    }

    private boolean hasActiveMembership(String memberId) {
        List<Membership> memberships = membershipRepository.findByMemberId(memberId);
        LocalDate today = LocalDate.now();
        return memberships.stream().anyMatch(m ->
                m.getStatus() == Status.MembershipStatus.ACTIVE && !today.isAfter(m.getEndDate()));
    }
}