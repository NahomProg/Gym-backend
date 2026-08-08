package org.example.gymbackend.service;

import org.example.gymbackend.dto.request.CreateClassSessionRequest;
import org.example.gymbackend.dto.request.EnrollRequest;
import org.example.gymbackend.dto.response.ClassSessionResponse;
import org.example.gymbackend.dto.response.EnrollmentResponse;
import org.example.gymbackend.entity.ClassEnrollment;
import org.example.gymbackend.entity.ClassSession;
import org.example.gymbackend.entity.Member;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.exception.GymApiException;
import org.example.gymbackend.repository.ClassEnrollmentRepository;
import org.example.gymbackend.repository.ClassSessionRepository;
import org.example.gymbackend.repository.MemberRepository;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;

@Service
public class ClassSessionService {

    private final ClassSessionRepository classSessionRepository;
    private final ClassEnrollmentRepository classEnrollmentRepository;
    private final MemberRepository memberRepository;

    public ClassSessionService(ClassSessionRepository classSessionRepository,
                               ClassEnrollmentRepository classEnrollmentRepository,
                               MemberRepository memberRepository) {
        this.classSessionRepository = classSessionRepository;
        this.classEnrollmentRepository = classEnrollmentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ClassSessionResponse createClass(CreateClassSessionRequest request) {
        ClassSession session = ClassSession.builder()
                .className(request.getClassName())
                .classDescription(request.getClassDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxCapacity(request.getMaxCapacity())
                .currentEnrollment(0)
                .status(Status.ClassStatus.SCHEDULED)
                .build();

        classSessionRepository.save(session);
        return ClassSessionResponse.fromEntity(session);
    }

    public List<ClassSessionResponse> getAllClasses() {
        return classSessionRepository.findAll().stream()
                .map(ClassSessionResponse::fromEntity)
                .toList();
    }

    public ClassSessionResponse getClassById(String id) {
        ClassSession session = classSessionRepository.findById(id)
                .orElseThrow(() -> GymApiException.classSessionNotFound(id));
        return ClassSessionResponse.fromEntity(session);
    }

    @Transactional
    public EnrollmentResponse enroll(String classSessionId, EnrollRequest request) {
        ClassSession session = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> GymApiException.classSessionNotFound(classSessionId));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> GymApiException.memberNotFound(request.getMemberId()));

        if (classEnrollmentRepository.existsByMemberIdAndClassSessionIdAndStatus(
                member.getId(), session.getId(), Status.EnrollmentStatus.ACTIVE)) {
            throw GymApiException.alreadyEnrolled(member.getId(), session.getId());
        }

        if (session.getCurrentEnrollment() >= session.getMaxCapacity()) {
            throw GymApiException.classFull(session.getId());
        }

        ClassEnrollment enrollment = ClassEnrollment.builder()
                .member(member)
                .classSession(session)
                .waitlisted(false)
                .status(Status.EnrollmentStatus.ACTIVE)
                .build();

        classEnrollmentRepository.save(enrollment);

        session.setCurrentEnrollment(session.getCurrentEnrollment() + 1);
        classSessionRepository.save(session);

        return EnrollmentResponse.fromEntity(enrollment);
    }

    public List<EnrollmentResponse> getEnrollmentsForClass(String classSessionId) {
        if (!classSessionRepository.existsById(classSessionId)) {
            throw GymApiException.classSessionNotFound(classSessionId);
        }
        return classEnrollmentRepository.findByClassSessionId(classSessionId).stream()
                .map(EnrollmentResponse::fromEntity)
                .toList();
    }
}