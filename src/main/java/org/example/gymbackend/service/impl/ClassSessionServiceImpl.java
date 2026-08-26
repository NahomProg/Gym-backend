package org.example.gymbackend.service.impl;

import org.example.gymbackend.service.ClassSessionService;
import org.example.gymbackend.mapper.EnrollmentMapper;
import org.example.gymbackend.mapper.ClassSessionMapper;

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
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ClassSessionServiceImpl implements ClassSessionService {
    public ClassSessionServiceImpl(
            EnrollmentMapper enrollmentMapper,
                             ClassSessionMapper classSessionMapper,
                             ClassSessionRepository classSessionRepository,
                             ClassEnrollmentRepository classEnrollmentRepository,
                             MemberRepository memberRepository) {
        this.enrollmentMapper = enrollmentMapper;
        this.classSessionMapper = classSessionMapper;
        this.classSessionRepository = classSessionRepository;
        this.classEnrollmentRepository = classEnrollmentRepository;
        this.memberRepository = memberRepository;
    }

    private final EnrollmentMapper enrollmentMapper;

    private final ClassSessionMapper classSessionMapper;

    private final ClassSessionRepository classSessionRepository;
    private final ClassEnrollmentRepository classEnrollmentRepository;
    private final MemberRepository memberRepository;


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
        return classSessionMapper.toResponse(session);
    }

    public List<ClassSessionResponse> getAllClasses() {
        return classSessionRepository.findAll().stream()
                .map(classSessionMapper::toResponse)
                .toList();
    }

    public ClassSessionResponse getClassById(String id) {
        ClassSession session = classSessionRepository.findById(id)
                .orElseThrow(() -> GymApiException.classSessionNotFound(id));
        return classSessionMapper.toResponse(session);
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

        boolean isFull = session.getCurrentEnrollment() >= session.getMaxCapacity();

        ClassEnrollment enrollment = ClassEnrollment.builder()
                .member(member)
                .classSession(session)
                .waitlisted(isFull)
                .status(Status.EnrollmentStatus.ACTIVE)
                .build();

        classEnrollmentRepository.save(enrollment);

        if (!isFull) {
            session.setCurrentEnrollment(session.getCurrentEnrollment() + 1);
            classSessionRepository.save(session);
            return enrollmentMapper.toResponse(enrollment);
        }

        long waitlistPosition = classEnrollmentRepository
                .findByClassSessionIdAndWaitlistedTrueAndStatusOrderByCreatedAtAsc(
                        session.getId(), Status.EnrollmentStatus.ACTIVE)
                .size();

        return enrollmentMapper.toResponse(enrollment, (int) waitlistPosition);
    }

    @Transactional
    public EnrollmentResponse cancelEnrollment(String classSessionId, String memberId) {
        ClassSession session = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> GymApiException.classSessionNotFound(classSessionId));

        ClassEnrollment enrollment = classEnrollmentRepository
                .findByMemberIdAndClassSessionIdAndStatus(memberId, classSessionId, Status.EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> GymApiException.enrollmentNotFound(memberId, classSessionId));

        boolean wasWaitlisted = enrollment.isWaitlisted();
        enrollment.setStatus(Status.EnrollmentStatus.CANCELLED);
        classEnrollmentRepository.save(enrollment);

        if (wasWaitlisted) {
            return enrollmentMapper.toResponse(enrollment);
        }

        session.setCurrentEnrollment(session.getCurrentEnrollment() - 1);
        classSessionRepository.save(session);

        classEnrollmentRepository
                .findFirstByClassSessionIdAndWaitlistedTrueAndStatusOrderByCreatedAtAsc(
                        classSessionId, Status.EnrollmentStatus.ACTIVE)
                .ifPresent(promoted -> {
                    promoted.setWaitlisted(false);
                    promoted.setWaitlistPromotedAt(LocalDateTime.now());
                    classEnrollmentRepository.save(promoted);

                    session.setCurrentEnrollment(session.getCurrentEnrollment() + 1);
                    classSessionRepository.save(session);
                });

        return enrollmentMapper.toResponse(enrollment);
    }

    public List<EnrollmentResponse> getWaitlist(String classSessionId) {
        if (!classSessionRepository.existsById(classSessionId)) {
            throw GymApiException.classSessionNotFound(classSessionId);
        }

        List<ClassEnrollment> waitlist = classEnrollmentRepository
                .findByClassSessionIdAndWaitlistedTrueAndStatusOrderByCreatedAtAsc(
                        classSessionId, Status.EnrollmentStatus.ACTIVE);

        List<EnrollmentResponse> result = new ArrayList<>();
        for (int i = 0; i < waitlist.size(); i++) {
            result.add(enrollmentMapper.toResponse(waitlist.get(i), i + 1));
        }
        return result;
    }

    public List<EnrollmentResponse> getEnrollmentsForClass(String classSessionId) {
        if (!classSessionRepository.existsById(classSessionId)) {
            throw GymApiException.classSessionNotFound(classSessionId);
        }
        return classEnrollmentRepository.findByClassSessionId(classSessionId).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }
}